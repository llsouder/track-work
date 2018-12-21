(ns track-work.project
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [reagent.core :as r]))

(defn handle-change-project
  [db [_ project]]
  (assoc db :project project))

(rf/reg-event-db :change-project handle-change-project)

(rf/reg-sub
 :project
 (fn [db _] (:project db)))

(defn error []
  (js/alert "error"))

(defn add-project  [description]
  (js/alert (str "adding " description)))

(defn type-form []
  (let [value (rf/subscribe [:project])]
  [:div
   [:form {:id "projecttypeform"}
    "Project:" [:br]
    [:input {:id "project"
             :value @value
             :name "project"
             :type "text"
             :on-change #(rf/dispatch
                 [:change-project (-> % .-target .-value)])}][:br]

    [:input {:value "submit"
             :type "button"
             :on-click (fn [event] (rf/dispatch [:add-project-click]))}][:br]]]))


(defn update-projects
  [db [_ projects]]
  (assoc db :projects projects))

(rf/reg-event-db :update-projects update-projects)

(defn get-projects [id]
  (ajax/GET (str "/api/get_projects/" id)
            {:handler #(rf/dispatch [:update-projects %1])
             :response-format :json
             :keywords? true}))

(defn handle-add-project
  [{:keys [project user_id] :as db} _]
  (if (string/blank? project )
    (error)
    (ajax/POST "/api/add_project"
      {:params {:user_id  user_id
                :proj_desc project}
       :format :json
       :handler #(do (rf/dispatch [:change-project ""])
                     (get-projects user_id))}))
  db)

(rf/reg-event-db :add-project-click handle-add-project)

(defn make-row
  [project]
   [:tr {:key (:id project)}
    [:td [:input {:type :button
                  :on-click #(rf/dispatch [:set-proj_id (:id project)])}]]
    [:td (:proj_desc project)]])

(rf/reg-sub
 :projects
 (fn [db _] (:projects db)))

(defn list-projects []
  (let [user_id (rf/subscribe [:user_id])
        projects (rf/subscribe [:projects])]
  (get-projects @user_id)
  (fn []
   [:table {:border "1"}
    [:tbody
     [:tr
      [:th "Id"] [:th "Project"]]
      (map make-row @projects)]])))

(defn component []
  [:div.project-data
   [type-form]
   [list-projects]])
