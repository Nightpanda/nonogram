(ns nonogram.core
  (:require [clojure.pprint :refer [print-table]]
            [clojure.java.io :refer [resource]])
  (:use mikera.image.core)
  (:gen-class))

(defn graphic-array->nonogram-format
  "Takes an array containing pixel-data and transforms it into nonogram format. An array containing
  amount of consecutive pixels."
  [pixel-data]
  (map #(count %) (filter #(some #{1} %) (partition-by #(= % 0) pixel-data))))

(defn graphic-arrays->nonogram-format [graphics]
    (filter not-empty
       (map (fn [graphic]
              (if (every? #(= 0 %) graphic)
                [0]
                (graphic-array->nonogram-format graphic))) graphics)))

(defn art->graphic-columns [art]
  (apply mapv vector art))

(defn art->nonogram [art]
  {:rows (graphic-arrays->nonogram-format art)
   :columns (graphic-arrays->nonogram-format (art->graphic-columns art))})

(defn nonogram-column->string [column]
  (clojure.string/join column))

(defn nonogram-row->string [row]
  (str (clojure.string/join (flatten row))))

(defn nonogram-rows->printtable [rows]
  (for [row (map #(flatten %) rows)] {:row (clojure.string/join row)}))

(defn nonogram-columns->printtable [columns]
  (let [column-names (for [column-number (range 0 (count columns))] (str "column" column-number))
        column-keywords (map #(keyword %) column-names)
        column-values (for [column (map #(flatten %) columns)] (nonogram-column->string column))
        columns-printable (for [index (range 0 (count column-keywords))]
                  {(nth column-keywords index) (nth column-values index)})]
        (into {} columns-printable)))

(defn join-printtable-rows-and-columns [rows columns]
  (conj rows columns))

(defn form-printtable-headers [print-rows print-columns]
  (concat (keys (first print-rows)) (map #(key %) print-columns)))

(defn draw-nonogram [nonogram]
  (str " " (clojure.string/join (concat (map #(nonogram-column->string %) (:columns nonogram))
                                        (map #(nonogram-row->string %) (:rows nonogram))))))

(defn random-image [square-size]
  (let [image (new-image square-size square-size)
        pixels (get-pixels image)]
        (dotimes [i (* square-size square-size)]
          (aset pixels i (rand-int 2)))
          (set-pixels image pixels)
          image))

(defn -main2 []
                                        ;(map #(print %) (slurp "./face.png"))
                                        ;  (let [pixels ((load-image-resource "./face.png"))])
  (let [;image (-> "./cat2.ICO " resource load-image)
        image (random-image 16)
        pixels (get-pixels image)
        ]
          (let [rows (partition 16 pixels)
          art (map (fn [row] (map #(if (= % 0) 0 1) row)) rows)]
          (println (map #(println %) art)))

        
    )

)

(defn -main
  [& args]
  (let [square-size 4
        image (random-image square-size);(-> "./cat2.png" resource load-image)
        pixels (get-pixels image)
        rows (partition square-size pixels)
        art (vec (map (fn [row] (vec (map #(if (= % 0) 0 1) row))) rows))
;        art (partition 64 (vec pixels))
        nonogram (art->nonogram art)
        print-columns (nonogram-columns->printtable (:columns nonogram))
        print-rows (nonogram-rows->printtable (:rows nonogram))
        headers (form-printtable-headers print-rows print-columns)
        print-data (join-printtable-rows-and-columns print-rows print-columns)]
    ;(println (map #(clojure.string/join %) art))
                                        ;(println (art->nonogram art))
      ;                   (println headers)
;                          (println print-columns)
;                          (println print-rows)
;                          (println print-data)
                                        ;    (print-table headers print-data)
    (println (map #(println %) art))
    (print-table headers print-data)))
