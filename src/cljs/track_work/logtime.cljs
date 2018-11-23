(ns track-work.logtime
  (:require [reagent.core :as r]
            [re-frame.core :as rf]
            [ajax.core :as ajax]
            [track-work.events]))

(rf/reg-sub
 :note
 (fn [db _] (:note db)))

(defn handle-change-note
  [db [_ note]]
  (assoc db :note note))

(rf/reg-event-db :change-note handle-change-note)

(defn get-user-state
  [id]
  (let [url (str "/api/get_user_state/" id)]
    (ajax/GET url
            {:handler #(rf/dispatch
                          [:change-note (:note %)])
             :response-format :json
             :keywords? true})))

(defn note-text-box []
  (let [value (rf/subscribe [:note])]
    (fn []
      [:input {:id "note-text-box"
               :type "text"
               :value @value
               :on-change #(rf/dispatch
                            [:change-note (-> % .-target .-value)])}])))

(defn update-state!
  [{:keys [user_id note] :as db} _]
  (println "post malone: " user_id note)
  (ajax/POST "/api/update_state"
             {:params {:user_id user_id
                       :note note}
              :format :json})
  db)

(rf/reg-event-db :save-click update-state!)

(defn save-note-button []
  [:input {:type "button"
           :value "save"
           :on-click (fn [event]
                       (rf/dispatch [:save-click]))}])

;;########################################################

(defn set-types! 
  [response]
  (rf/dispatch [:set-time-types response]))

(rf/reg-sub
 :time-types
 (fn [db _] (:time-types db)))

(defn get_types []
  (println "getting types")
  (ajax/GET "/api/get_types" {:handler set-types!
                              :response-format :json
                              :keywords? true}))

(defn make-button [{:keys [id time_type] :as args}]
  [:div {:key time_type} [:input {:class "btn btn-primary"
           :type "button"
           :value time_type}]])

(defn time-buttons [times]
  (map make-button times))

(defn mainpanel []
  (get_types)

  (let [user-id (rf/subscribe [:user_id])
        time-types (rf/subscribe [:time-types])]
    (fn[]
        (do
          (get-user-state @user-id)
          [:div
          (time-buttons @time-types)
          [note-text-box]
           [save-note-button]
;;           ])))))
           ]))))
