(ns track-work.bubble
  (:require [ajax.core :as ajax]
            [re-frame.core :as rf]))

;;Bubble
;;Why bubble?
;;the gui has checkboxes which I called bubbles
;;because I could not think of a good name.
;;I am keeping the name until think of something
;;better.  Currently I am calling the same data
;;in postgres under the column qtr_hr."

(defn add-bubble [id get-task-fn]
  (ajax/POST "/api/add_bubble"
             {:params {:task_id id}
              :format :json
              :handler #(get-task-fn id)
              :error #(println (str "error:" id))}))

(defn remove-bubble [id get-task-fn]
  (ajax/POST "/api/remove_bubble"
             {:params {:task_id id}
              :format :json
              :handler #(get-task-fn id)
              :error #(println (str "error:" id))}))

(defn check-bubble
  "Returns true if the event has a checkbox that is checked."
  [event]
  (-> event .-target .-checked))

(defn toggle-bubble
  "it means add or remove qtr_hr in the task."
  [event task_id get-task-fn]
  (if (check-bubble event)
    (add-bubble task_id get-task-fn)
    (remove-bubble task_id get-task-fn)))

(defn make-bubbles [task_id checks get-tasks-fn]
  (map
   (fn [n check]
     [:input {:id (str task_id "-" n)
              :type "checkbox"
              :checked (< n checks)
              :on-click #(toggle-bubble %1 task_id get-tasks-fn)}])
   (range 8)))
