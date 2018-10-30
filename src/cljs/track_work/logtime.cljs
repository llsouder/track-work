(ns track-work.logtime
  (:require [reagent.core :as r]
            [ajax.core :as ajax]))

(def timetypes (r/atom nil))

(defn set-types! [response]
  (reset! timetypes response))

(defn get_types []
  (ajax/GET "/api/get_types" {:handler set-types!
                              :response-format :json
                              :keywords? true}))

(defn make-button [{:keys [id time_type] :as args}]
  [:input {:class "btn btn-primary" :type "button" :value time_type :key id :id id}])

(defn time-buttons [times]
  (map make-button times))

(defn mainpanel []
  (get_types)
  (fn []
    [:div "work!!!"
   (time-buttons @timetypes)]))
