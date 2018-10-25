(ns track-work.timetypes
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [reagent.core :as r]))

(defn handle-change-name
  [db [_ name]]
  (assoc db :name name))

(rf/reg-event-db :change-name handle-change-name)

(defn error []
  (js/alert "error"))

(defn add-type [name description]
  (js/alert (str "adding " name)))

(defn type-form []
  [:div
   [:form
    "Time Type:" [:br]
    [:input {:id "name"
             :name "name"
             :type "text"
             :on-change #(rf/dispatch
                          [:change-name (-> % .-target .-value)])}][:br]
    "Description:" [:br]
    [:input {:id "description"
             :name "description"
             :type "text"}][:br]

    [:input {:value "submit"
             :type "button"
             :on-click (fn [event] (rf/dispatch [:add-type-click]))}][:br]]])

(defn handle-add-type
  [{:keys [name description] :as db} _]
  (println "read a book")
  (if (string/blank? name )
    (error)
    (add-type name description))
  db)

(rf/reg-event-db :add-type-click handle-add-type)

(def timetypes (r/atom nil))

(defn make-row
  [timetype]
   [:tr {:key (:id timetype)}
    [:td (:id timetype)]
    [:td (:name timetype)]
    [:td (:description timetype)]])

(defn set-types! [response]
  (reset! timetypes response))

(defn get-types []
  (ajax/GET "/get_types" {:handler set-types!
                          :response-format :json
                          :keywords? true}))

(defn list-types []
  (get-types)
  (fn []
   [:table {:border "1"}
    [:tbody
     [:tr
      [:th "Id"] [:th "Name"] [:th "Description"]]
      (map make-row @timetypes)]]))

(defn page []
  [:div
   [type-form]
   [list-types]])
