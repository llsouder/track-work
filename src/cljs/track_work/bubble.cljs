(ns track-work.bubble)

(defn bubbles [group]
  (map
   (fn [n] [:input {:id (str group "-" n) :type "checkbox"}])
   (range 4)))

(defn one-hour 
  "4 groups of 4 checkboxes for the given task_id."
  [task_id]
  [:div {:class "row"}
   (map
    (fn [n] [:div.bubble-hour (bubbles n) [:div.bubble-spacer]])
    (range 4))])

(defn mainpanel []
  [:div "Bubbles"
   (one-hour)])

