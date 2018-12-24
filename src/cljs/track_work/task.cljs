(ns track-work.task
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [track-work.bubble :as bubble]
            [reagent.core :as r]))

(defn handle-change-task
  [db [_ task]]
  (assoc db :task task))

(rf/reg-event-db :change-task handle-change-task)

(rf/reg-sub
 :task
 (fn [db _] (:task db)))

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

(defn update-task
  [db [_ tasks]]
  (assoc db :tasks tasks))

(rf/reg-event-db :update-task update-task)

(defn get-tasks [id]
  (if id
    (ajax/GET (str "/api/get_tasks/" id)
      {:handler #(rf/dispatch [:update-task %1])
       :response-format :json
       :keywords? true})))

(defn handle-add-task
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

(defn make-row
  [{:keys [id task_desc] :as task}]
  [:tr {:key id}
   [:td task_desc]
   [:td (bubble/one-hour id)]])

(rf/reg-sub
 :tasks
 (fn [db _] (:tasks db)))

(defn list-task []
  (let [proj_id (rf/subscribe [:proj_id])
        proj_desc (rf/subscribe [:proj_desc])
        tasks (rf/subscribe [:tasks])]
    (fn []
      (get-tasks @proj_id)
      [:div
       [:h4 (if (nil? @proj_desc)
              "no project selected"
              @proj_desc)]
       [:table.table 
        [:tbody
         (map make-row @tasks)]]])))

(defn dropdown-task []
  (let [proj_id (rf/subscribe [:proj_id])
        proj_desc (rf/subscribe [:proj_desc])
        tasks (rf/subscribe [:tasks])]
    (fn []
      (get-tasks @proj_id)
      [:div
       [:h4 (if (nil? @proj_desc)
              "no project selected"
              @proj_desc)]
       [:table.table 
        [:tbody
         (map make-row @tasks)]]])))

(defn component []
  [:div.task-component
   [list-task]
   [task-form]])

(defn page []
  [:div.task-page
   [list-task]
   [task-form]])

(rf/reg-event-db
 :set-task_error
 (fn [db [_ task_error]]
   (assoc db :task_error task_error)))

(rf/reg-sub
 :task_error
 (fn [db _]
   (:task_error db)))
