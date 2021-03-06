(ns track-work.task
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [track-work.bubble :as bubble]
            [reagent.core :as r]))

(defn task-form []
  (let [value (rf/subscribe [:task])
        task_error (rf/subscribe [:task_error])]
    [:div.task-form
     [:form {:id "taskform"}
      [:input {:id "task"
               :value @value
               :placeholder "Add a task"
               :name "task"
               :type "text"
               :on-change #(rf/dispatch
                            [:change-task
                             (-> % .-target .-value)])
               :on-click #(rf/dispatch [:set-task_error nil])}]
      [:input {:value "Add"
               :type "button"
               :on-click (fn [event]
                           (rf/dispatch [:add-task-click]))}]]
     (if @task_error
       [:div.alert.alert-danger  @task_error])]))

(defn get-tasks [id]
  (if id
    (ajax/GET (str "/api/get_tasks/" id)
              {:handler #(rf/dispatch [:update-task %1])
               :response-format :json
               :keywords? true})))

(defn handle-add-task
  "Add a new task."
  [{:keys [task proj_id] :as db} _]
  (if (string/blank? task)
    (rf/dispatch [:set-task_error "Task cannot be blank."])
    (ajax/POST "/api/add_task"
               {:params {:proj_id  proj_id
                         :task_desc task}
                :format :json
                :error #(rf/dispatch [:set-task_error
                                      (str "Task not added, proj_id:" proj_id)])
                :handler #(do (rf/dispatch [:change-task ""])
                              (println "posting task:")
                              (get-tasks proj_id))}))
  db)

(rf/reg-event-db :add-task-click handle-add-task)

(defn extract-from
  [id tasks]
  (let [bubbles (vec (filter #(= id (:id %1)) tasks))
        final ((comp vec flatten conj) bubbles (repeat 8 {}))
        nomore (take 8 final)]
    (take 8 ((comp vec flatten conj) bubbles (repeat 8 {})))))

(defn just-the-tasks
  [tasks]
  (distinct (map #(dissoc %1 :bubble) tasks)))

(defn make-row
  [task tasks]
  (let [{:keys [id task_desc]} task
        bubbles (extract-from id tasks)]
    [:tr {:key id}
     [:td task_desc]
     [:td (bubble/make-bubbles id bubbles get-tasks)]]))

(defn list-task []
  (let [proj_id (rf/subscribe [:proj_id])
        proj_desc (rf/subscribe [:proj_desc])
        tasks (rf/subscribe [:tasks])]
    (fn []
      (get-tasks @proj_id)
      (let [task-list (just-the-tasks @tasks)]
        [:div
         [:h4 (if (nil? @proj_desc)
                "No project selected."
                @proj_desc)]
         [:table.table 
          [:tbody
           (map #(make-row %1 @tasks) task-list)]]]))))

(defn component []
  [:div.task-component
   [list-task]
   [task-form]])

(defn page []
  [:div.task-page
   [list-task]
   [task-form]])

(defn handle-change-task
  [db [_ task]]
  (assoc db :task task))

(rf/reg-event-db
 :change-task handle-change-task)

(rf/reg-sub
 :task
 (fn [db _] (:task db)))

(defn update-task
  [db [_ tasks]]
  (assoc db :tasks tasks))

(rf/reg-event-db
 :update-task update-task)

(rf/reg-sub
 :tasks
 (fn [db _] (:tasks db)))

(rf/reg-event-db
 :set-task_error
 (fn [db [_ task_error]]
   (assoc db :task_error task_error)))

(rf/reg-sub
 :task_error
 (fn [db _]
   (:task_error db)))
