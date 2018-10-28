(ns track-work.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [track-work.db.core :as db]
            [clojure.xml :as xml]))

(s/defschema TimeType {:timetype String
                       :description String})

(defn create-timetype! [timetype]
  (db/create-timetype! {:time_type (:timetype timetype)
                        :description (:description timetype)}))
(defapi service-routes
  {:swagger
   {:ui "/swagger-ui"
    :spec "/swagger.json"}}
  (context "/api" []

   (GET "/get_types" []
     (ok (db/get-timetypes)))

   (POST "/add_type" []
         :return TimeType
         :body [timetype TimeType]
         :summary "This is suppose to add a new time type."
         (do
          (create-timetype! timetype)
          (ok timetype)))))
