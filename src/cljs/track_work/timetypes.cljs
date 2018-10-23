(ns track-work.timetypes
  (:require [ajax.core :as ajax]
            [reagent.core :as r]))

(defn type-form []
  [:form {:action "/add_type" :method "POST"}
   [:input {:id "name" :name "name" :type "text"}]
   [:input {:id "description" :name "description" :type "text"}]
   [:input {:id "submit" :type "submit"}]])

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
