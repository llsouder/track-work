(ns track-work.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [track-work.db.core :as db]
            [clojure.xml :as xml]))

(s/defschema TimeType {:timetype String
                       :description String})

(s/defschema UserState {:user_id s/Int
                        (s/optional-key :click_time) s/Int
                        :note String})

(defn create-timetype! [timetype]
  (db/create-timetype! {:time_type (:timetype timetype)
                        :description (:description timetype)}))

(defn update-user-state! [userstate]
  (db/update-user-state! (assoc userstate :click_time nil)))

(defapi service-routes
  {:swagger
   {:ui "/swagger-ui"
    :spec "/swagger.json"}}
  (context "/api" []

   (GET "/get_types" []
        (ok (db/get-timetypes)))

   (GET "/get_user_state/:id" []
        :path-params [id :- Long]
        (let [state (db/get-user-state {:user_id id})]
              (ok (into {} (filter second state)))))

   (POST "/add_type" []
         :return TimeType
         :body [timetype TimeType]
         :summary "This is suppose to add a new time type."
         (do
          (create-timetype! timetype)
          (ok timetype)))

(POST "/update_state" []
      :return UserState
      :body [userstate UserState]
      :summary "This will update the user's current state."
      (do
        (println userstate)
        (ok userstate)))))

;;(def state (track-work.db.core/get-user-state {:user_id 100}))
;;(println state)
;;(track-work.db.core/update-user-state! (update state :note #(str "more " %1)))
