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

(defn project-form []
  (let [value (rf/subscribe [:project])
       proj_error (rf/subscribe [:proj_error])]
  [:div.project-form
   [:form {:id "projectform"}
    [:input {:id "project"
             :value @value
             :on-click #(rf/dispatch [:set-proj_error nil])
             :name "project"
             :placeholder "Add a new project"
             :type "text"
             :on-change #(rf/dispatch
                 [:change-project (-> % .-target .-value)])}]

    [:input {:value "Add"
             :type "button"
             :on-click (fn [event]
                         (rf/dispatch [:add-project-click]))}]]
   (if @proj_error
     [:div.alert.alert-danger  @proj_error])]))


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
    (rf/dispatch [:set-proj_error "Project cannot be blank."])
    (ajax/POST "/api/add_project"
      {:params {:user_id  user_id
                :proj_desc project}
       :format :json
       :handler #(do (rf/dispatch [:change-project ""])
                     (get-projects user_id))}))
  db)

(rf/reg-event-db :add-project-click handle-add-project)

(defn set-current-project
  [{:keys [id proj_desc]}]
  (do
    (rf/dispatch [:set-proj_desc proj_desc])
    (rf/dispatch [:set-proj_id id])))

(defn make-row
  [project]
  [:button.btn.btn-info {:class "nav-item" :on-click #(set-current-project project)
        :key (:id project)}
    [:a (:proj_desc project)]])

(rf/reg-sub
 :projects
 (fn [db _] (:projects db)))

(defn list-projects []
  (let [user_id (rf/subscribe [:user_id])
        projects (rf/subscribe [:projects])]
  (get-projects @user_id)
  (fn []
    [:ul {:class "navbar-nav mr-auto"}
     (map make-row @projects)
     [project-form]])))

(defn component []
  [:nav.navbar.navbar-light.bg.light {:id "sidebar" }
   [:div {:class "container-fluid"}
    [:div {:class "navbar-header"}
     [:a {:class "navbar-brand" :href "#"}]]
    [list-projects]]])

(defn page []
  [:div.project-page
   [list-projects]
   [project-form]])

(rf/reg-event-db
 :set-proj_error
 (fn [db [_ proj_error]]
   (assoc db :proj_error proj_error)))

(rf/reg-sub
 :proj_error
 (fn [db _]
   (:proj_error db)))
