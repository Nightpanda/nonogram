(ns aws.handler
  (:gen-class
   :implements [com.amazonaws.services.lambda.runtime.RequestHandler])
  (:import [com.amazonaws.services.lambda.runtime.events APIGatewayProxyResponseEvent])
  (:require [nonogram.core :as nonogram]))

(defn lambda-integration-response [^String msg ^long status-code]
  "Wraps message into lambda integration response object"
  (doto (new APIGatewayProxyResponseEvent)
    (.setStatusCode (int status-code))
    (.setHeaders {"Content-Type" "application/json" "Access-Control-Allow-Origin" "*"})
    (.setBody msg)))

(defn -handleRequest
  "Returns random nonogram"
  [this input context]
    (lambda-integration-response (nonogram/random-nonogram 8) 200))
