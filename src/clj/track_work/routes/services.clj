(ns track-work.routes.services
  (:require [ring.util.http-response :refer :all]
            [compojure.api.sweet :refer :all]
            [schema.core :as s]
            [track-work.db.core :as db]
            [clojure.xml :as xml]))

(defapi service-routes
  {:swagger
   {:ui "/swagger-ui"
    :spec "/swagger.json"}}
  (context "/api" []

   (GET "/get_types" []
     (ok (db/get-timetypes)))

   (POST "/add_type" []
         :query-params [time_type :- String
                        description :- String]
         (ok (db/create-timetype! {:time_type time_type
                                  :description description})
            time_type))))
