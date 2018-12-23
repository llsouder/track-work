(ns track-work.events
  (:require [re-frame.core :as rf]))

;;dispatchers

(rf/reg-event-db
  :navigate
  (fn [db [_ page]]
    (assoc db :page page)))

(rf/reg-event-db
  :set-docs
  (fn [db [_ docs]]
    (assoc db :docs docs)))

(rf/reg-event-db
 :set-user_id
 (fn [db [_ user_id]]
   (assoc db :user_id user_id)))

(rf/reg-event-db
 :set-time-types
 (fn [db [_ time-types]]
   (assoc db :time-types time-types)))

(rf/reg-event-fx
  :fetch-docs
  (fn [_ _]
    {:http {:url "/docs"
            :method :get
            :success-event [:set-docs]}}))

(rf/reg-event-db
  :common/set-error
  (fn [db [_ error]]
    (assoc db :common/error error)))

;;subscriptions

(rf/reg-sub
  :page
  (fn [db _]
    (:page db)))

(rf/reg-sub
  :docs
  (fn [db _]
    (:docs db)))

(rf/reg-sub
  :common/error
  (fn [db _]
    (:common/error db)))

(rf/reg-sub
 :user_id
 (fn [db _]
   (:user_id db)))

(rf/reg-event-db
 :set-proj_id
 (fn [db [_ proj_id]]
   (assoc db :proj_id proj_id)))

(rf/reg-sub
 :proj_id
 (fn [db _]
   (:proj_id db)))

(rf/reg-event-db
 :set-proj_desc
 (fn [db [_ proj_desc]]
   (assoc db :proj_desc proj_desc)))

(rf/reg-sub
 :proj_desc
 (fn [db _]
   (:proj_desc db)))
