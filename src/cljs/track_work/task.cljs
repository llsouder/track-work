(ns track-work.task
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [reagent.core :as r]))

(defn handle-change-task
  [db [_ task]]
  (assoc db :task task))

(rf/reg-event-db :change-task handle-change-task)

(rf/reg-sub
 :task
 (fn [db _] (:task db)))

(defn task-form []
  (let [value (rf/subscribe [:task])]
  [:div.task-form
   [:form {:id "task"}
    "Add task:" [:br]
    [:input {:id "task"
             :value @value
             :name "task"
             :type "text"
             :on-change #(rf/dispatch
                 [:change-task (-> % .-target .-value)])}][:br]

    [:input {:value "Add"
             :type "button"
             :on-click (fn [event] (rf/dispatch [:add-task-click]))}][:br]]]))


(defn update-task
  [db [_ tasks]]
  (assoc db :tasks tasks))

(rf/reg-event-db :update-task update-task)

(defn get-tasks [id]
  (if (nil? id)
    (println "move on id was nil")
    (ajax/GET (str "/api/get_tasks/" id)
            {:handler #(rf/dispatch [:update-task %1])
             :response-format :json
             :keywords? true})))

(defn handle-add-task
  [{:keys [task proj_id] :as db} _]
  (if (string/blank? task )
    (js/alert "Task cannot be blank.")
    (ajax/POST "/api/add_task"
      {:params {:proj_id  proj_id
                :task_desc task}
       :format :json
       :error #(js/alert (str "Task not added, proj_id:" proj_id ))
       :handler #(do (rf/dispatch [:change-task ""])
                     (println "posting task:")
                     (get-tasks proj_id))}))
  db)

(rf/reg-event-db :add-task-click handle-add-task)

(defn make-row
  [task]
   [:tr {:key (:id task)}
    [:td (:id task)]
    [:td (:task_desc task)]])

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
           "no project choosen"
           @proj_desc)]
   [:table {:border "1"}
    [:tbody
     [:tr
      [:th "Id"] [:th "task"]]
     (map make-row @tasks)]]])))

(defn component []
  [:div.task-data
   [list-task]
   [task-form]])

(defn page []
  [:div.task-data
   [list-task]
   [task-form]])
