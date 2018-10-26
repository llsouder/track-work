(ns track-work.timetypes
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]
            [clojure.string :as string]
            [reagent.core :as r]))

(def timetypes (r/atom nil))

(defn handle-change-timetype
  [db [_ timetype]]
  (assoc db :timetype timetype))

(rf/reg-event-db :change-timetype handle-change-timetype)

(defn handle-change-desc
  [db [_ description]]
  (assoc db :description description))

(rf/reg-event-db :change-desc handle-change-desc)

(defn error []
  (js/alert "error"))

(defn add-type [timetype description]
  (js/alert (str "adding " timetype description)))

(defn type-form []
  [:div
   [:form
    "Time Type:" [:br]
    [:input {:id "timetype"
             :name "timetype"
             :type "text"
             :on-change #(rf/dispatch
                          [:change-timetype (-> % .-target .-value)])}][:br]
    "Description:" [:br]
    [:input {:id "description"
             :name "description"
             :type "text"
             :on-change #(rf/dispatch
                 [:change-desc (-> % .-target .-value)])}][:br]

    [:input {:value "submit"
             :type "button"
             :on-click (fn [event] (rf/dispatch [:add-type-click]))}][:br]]])

(defn set-types! [response]
  (reset! timetypes response))


(defn get-types []
  (println "getting the types")
  (ajax/GET "/get_types" {:handler set-types!
                          :response-format :json
                          :keywords? true}))

(defn handle-add-type
  [{:keys [timetype description] :as db} _]
  (if (string/blank? timetype )
    (error)
    (do
      (println (str "POST " timetype " " description))
      (ajax/POST "add_type"
                {:params {:time_type timetype
                          :description description}
                 :handler get-types})))
  db)

(rf/reg-event-db :add-type-click handle-add-type)

(defn make-row
  [timetype]
   [:tr {:key (:id timetype)}
    [:td (:id timetype)]
    [:td (:time_type timetype)]
    [:td (:description timetype)]])

(defn list-types []
  (get-types)
  (fn []
   [:table {:border "1"}
    [:tbody
     [:tr
      [:th "Id"] [:th "Time Type"] [:th "Description"]]
      (map make-row @timetypes)]]))

(defn page []
  [:div
   [type-form]
   [list-types]])
