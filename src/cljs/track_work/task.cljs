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

(defn error []
  (js/alert "error"))

(defn add-task  [description]
  (js/alert (str "adding " description)))

(defn type-form []
  (let [value (rf/subscribe [:task])]
  [:div
   [:form {:id "task"}
    "task:" [:br]
    [:input {:id "task"
             :value @value
             :name "task"
             :type "text"
             :on-change #(rf/dispatch
                 [:change-task (-> % .-target .-value)])}][:br]

    [:input {:value "submit"
             :type "button"
             :on-click (fn [event] (rf/dispatch [:add-task-click]))}][:br]]]))


(defn update-task
  [db [_ tasks]]
  (println "task:" tasks)
  (assoc db :tasks tasks))

(rf/reg-event-db :update-task update-task)

(defn get-tasks [id]
  (ajax/GET (str "/api/get_tasks/" id)
            {:handler #(rf/dispatch [:update-task %1])
             :response-format :json
             :keywords? true}))

(defn handle-add-task
  [{:keys [task project_id] :as db} _]
  (if (string/blank? task )
    (error)
    (ajax/POST "/api/add_task"
      {:params {:proj_id  project_id
                :task_desc task}
       :format :json
       :handler #(do (rf/dispatch [:change-task ""])
                     (get-tasks project_id))}))
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
        tasks (rf/subscribe [:tasks])]
  (fn []
    (get-tasks @proj_id)
   [:table {:border "1"}
    [:tbody
     [:tr
      [:th "Id"] [:th "task"]]
      (map make-row @tasks)]])))

(defn component []
  [:div.task-data
   [type-form]
   [list-task]])

;;doodles
