(ns track-work.bubble
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]))

(defn get-bubbles [id]
  (if id
    (ajax/GET (str "/api/get_bubbles/" id
                   {:handler #(rf/dispatch :set-bubble-map [[id %1]])}))))

(defn add-bubble [id]
  (ajax/POST "/api/add_bubble"
             {:params {:task_id id}
              :format :json
              :handler #((get-bubbles id))
              :error #(println (str "error:" id))}))

(defn remove-bubble [id]
  (ajax/POST "/api/remove_bubble"
             {:params {:task_id id}
              :format :json
              :handler #((get-bubbles id))
              :error #(println (str "error:" id))}))

(defn check-bubble
  "Returns true if the event has a checkbox that is checked."
  [event]
  (-> event .-target .-checked))

(defn toggle-bubble
  [event task_id]
  (if (check-bubble event)
    (add-bubble task_id)
    (remove-bubble task_id)))

(defn make-bubbles [task_id checks]
  (map
   (fn [n check]
     [:input {:id (str task_id "-" n)
              :type "checkbox"
              :checked (not-empty check)
              :on-click #(toggle-bubble %1 task_id)}])
   (range (count checks))
   checks))

(defn mainpanel []
  [:div "Bubbles" ])

(rf/reg-event-db
 :set-bubble-map
 (fn [db [_ bubbles_from_db]]))
