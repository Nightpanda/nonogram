(ns nonogram.core
  (:require [clojure.pprint :refer [print-table]]
            [clojure.java.io :refer [resource as-file]])
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
  (let [column-names (for [column-number (range 0 (count columns))] (str column-number))
        column-keywords (map #(keyword %) column-names)
        column-values (for [column (map #(flatten %) columns)] (nonogram-column->string column))
        columns-printable (map #(into {} %) 
          (for [index (range 0 (apply max (map count columns)))] 
            (map-indexed 
              (fn [idx col-values] 
                (if (< index (count col-values)) 
                  {(keyword (str idx)) (str (nth (reverse col-values) index))})) columns)))]
    columns-printable))

(defn join-printtable-rows-and-columns [rows columns]
  (concat rows columns))

(defn form-printtable-headers [print-rows print-columns]
  (let [rows (keys (first print-rows))
        cols (sort-by (fn [col] (read-string (name (key col)))) (first print-columns))]
  (concat rows (keys cols))))

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

(defn print-art [art]
  (println (map #(println %) art))
  art)

(defn find-image [path]
  ;;TODO add error checking for file not found
  (let [image (-> path resource load-image)]
    image))

(defn image->art [image]
  (let [square-size (width image)
        pixels (get-pixels image)
        rows (partition square-size pixels)
        art (vec (map (fn [row] (vec (map #(if (or (= % 0) (= % -1)) 0 1) row))) rows))]
        art))

(defn print-nonogram [nonogram]
  (let [print-columns (nonogram-columns->printtable (:columns nonogram))
        print-rows (nonogram-rows->printtable (:rows nonogram))
        headers (form-printtable-headers print-rows print-columns)
        print-data (join-printtable-rows-and-columns print-rows print-columns)]
    (print-table headers print-data)))

(defn random-nonogram [size]
  (->> (random-image size)
       (image->art)
       (art->nonogram)
       (print-nonogram)))

(defn image-file->max-size-nonogram [max-width image]
  (let [re-sized-image (if (> (width image) max-width)
                         (resize image max-width)
                         image)]
    (->> (image->art re-sized-image)
         (art->nonogram))))

(defn image-file->nonogram [image]
    (->> (image->art image)
         (art->nonogram)))

(defn image-nonogram-max-size [path max-size]
  (->> (find-image path)
       (image-file->max-size-nonogram max-size)
       (print-nonogram)))

(defn image-nonogram [path]
  (->> (find-image path)
       (image-file->nonogram)
       (print-nonogram)))

(defn -main [& args]
  (image-nonogram-max-size (first args) (read-string (last args))))
