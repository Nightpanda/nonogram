(ns nonogram.core
  (:gen-class))

(defn graphic-array->nonogram-format
  "Takes an array containing pixel-data and transforms it into nonogram format. An array containing
  amount of consecutive pixels."
  [pixel-data]
  (map #(count %) (filter #(some #{1} %) (partition-by #(= % 0) pixel-data))))

(defn graphic-arrays->nonogram-format [graphics]
  (filter not-empty (map #(graphic-array->nonogram-format %) graphics)))

(defn art->graphic-columns [art]
  (apply mapv vector art))

(defn art->nonogram [art]
  {:rows (graphic-arrays->nonogram-format art)
   :columns (graphic-arrays->nonogram-format (art->graphic-columns art))})

(defn nonogram-column->string [column]
  (clojure.string/join "\n" column))

(defn nonogram-row->string [row]
  (str (clojure.string/join (flatten row)) "\n"))

(defn draw-nonogram [nonogram]
  (str " " (clojure.string/join (concat (map #(nonogram-column->string %) (:columns nonogram))
                                        "\n"
                                        (map #(nonogram-row->string %) (:rows nonogram))))))

(defn -main
  [& args]
  (let [art [[0 0 1 0 0 1 0 0]
             [0 0 1 0 0 1 0 0]
             [1 0 0 0 0 0 0 1]
             [0 1 0 0 0 0 1 0]
             [0 0 1 1 1 1 0 0]]]
    (println (map #(clojure.string/join %) art))
    (println (art->nonogram art))
    (println (draw-nonogram (art->nonogram art)))))
