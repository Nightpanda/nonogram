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

(defn random-image [square-size]
  (let [image (new-image square-size square-size)
        pixels (get-pixels image)]
        (dotimes [i (* square-size square-size)]
          (aset pixels i (rand-int 2)))
          (set-pixels image pixels)
          image))

(defn print-art [art]
  (let [squashed-art (clojure.string/join "\n" (map #(clojure.string/join "" %) art))]
    (println squashed-art)))

(defn print-ascii-art [art ascii-mappings]
"Uses ascii-mappings to draw greyscaled art filling grey with they value of key :1 and the white with the value of key :0. For example {:1 'X' :0 '0'}"
  (let [squashed-art (clojure.string/join "\n" (map #(clojure.string/join "" %) art))
        replaced-1 (clojure.string/replace #"1" (str (:1 ascii-mappings)) squashed-art)
        manipulated-art (-> (clojure.string/replace  squashed-art #"1" (str (:1 ascii-mappings)))
                             (clojure.string/replace #"0" (str (:0 ascii-mappings))))]
    (println manipulated-art)))

(defn find-image [path]
(let [image (try (-> path resource load-image)
		 (catch IllegalArgumentException e (str "No file found with exception: " (.getMessage e))))]
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

(defn draw-random-nonogram [size]
  (->> (random-image size)
       (image->art)
       (print-art)))

(defn draw-random-nonogram-ascii [size ascii-mappings]
  (-> (random-image size)
       (image->art)
       (print-ascii-art ascii-mappings)))

(defn draw-nonogram-ascii [image size ascii-mappings]
  (-> image
       (image->art)
       (print-ascii-art ascii-mappings)))

(defn draw-nonogram [image size]
  (-> image
      (image->art)
      (print-art)))

(defn image-file->max-size-nonogram [max-width image]
  (let [re-sized-image (if (> (width image) max-width)
                         (resize image max-width)
                         image)]
    (->> (image->art re-sized-image)
         (art->nonogram))))

(defn image-nonogram-max-size [path max-size]
  (->> (find-image path)
       (image-file->max-size-nonogram max-size)
       (print-nonogram)))

(defn -main [& args]
  (let [first-arg (first args)
        second-arg (second args)
        third-arg (if (> (count args) 2)
                    (read-string (nth args 2)))
        size (read-string (last args))]
	(if (not (= (type size) java.lang.Long))
	  (throw (AssertionError. "Last argument must be a convertible to java.lang.Long with (read-string) for the size of nonogram")))
    (if (= first-arg "random")
      (if (= second-arg "draw")
        (if (and (some? (:0 third-arg)) (some? (:1 third-arg)))
          (draw-random-nonogram-ascii size third-arg)
          (draw-random-nonogram size))
        (random-nonogram size))
      (if (= second-arg "draw")
        (if (and (some? (:0 third-arg)) (some? (:1 third-arg)))
          (draw-nonogram-ascii (find-image first-arg) size third-arg)
          (draw-nonogram (find-image first-arg) size))
        (image-nonogram-max-size first-arg size)))))
