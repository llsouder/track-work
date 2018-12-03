(ns track-work.bubble)

(defn bubbles [group]
  (map (fn [n] [:input {:id (str group "-" n) :type "checkbox"}]) (range 4)))
(defn one-hour []
  [:div {:class "row"}
   (map (fn [n] [:div.bubble-hour (bubbles n) [:div.bubble-spacer]]) (range 4))])

(defn mainpanel []
  [:div "Bubbles"
   (one-hour)])

