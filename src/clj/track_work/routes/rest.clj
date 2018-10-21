(ns track-work.routes.rest
  (:require [track-work.layout :as layout]
            [track-work.db.core :as db]
            [compojure.core :refer [defroutes GET]]
            [compojure.api.sweet :as sweet]
            [ring.util.http-response :as response]
            [clojure.java.io :as io]))

(def pong
  (sweet/api
   (sweet/GET "/pong" []
              (response/ok {:pong "ping"}))))
(def get-users
  (sweet/api
   (sweet/GET "/get-users" []
              (response/ok (db/get-users)))))

(defroutes rest-routes
  get-users
  pong)
