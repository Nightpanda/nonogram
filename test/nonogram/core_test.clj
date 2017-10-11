(ns nonogram.core-test
  (:require [clojure.test :refer :all]
            [nonogram.core :refer :all]))

(deftest graphic-array->nonogram-format-test
  (testing "A graphic array where pixels are given as 1 or 0 results in a nonogram formatted
            list where consecutive 1s are summed, separated by 0s."
    (is (= (graphic-array->nonogram-format [0]) []))
    (is (= (graphic-array->nonogram-format [1]) [1]))
    (is (= (graphic-array->nonogram-format [1 1]) [2]))
    (is (= (graphic-array->nonogram-format [1 1 0]) [2]))
    (is (= (graphic-array->nonogram-format [0 1 0]) [1]))
    (is (= (graphic-array->nonogram-format [1 1 0 1]) [2 1]))
    (is (= (graphic-array->nonogram-format [1 1 0 1 1]) [2 2]))
    (is (= (graphic-array->nonogram-format [1 1 1 0 1 1]) [3 2]))))

(deftest graphic-arrays->nonogram-format-test
  (testing "A array of graphic arrays should produce an array of nonogram formatted arrays."
    (is (= (graphic-arrays->nonogram-format [[1]]) [[1]]))
    (is (= (graphic-arrays->nonogram-format [[1 1]]) [[2]]))
    (is (= (graphic-arrays->nonogram-format [[1 1] [1]]) [[2] [1]]))
    (is (= (graphic-arrays->nonogram-format [[1 1] [] [1 1 1]]) [[2] [3]]))))

(deftest art->graphics-columns-test
  (testing "An art image consisting of pixel data in 1s and 0s should result in that data
            being split according to columns."
    (is (= (art->graphic-columns [[1]]) [[1]]))
    (is (= (art->graphic-columns [[1 1]]) [[1] [1]]))
    (is (= (art->graphic-columns [[1 0] [1 1]]) [[1 1] [0 1]]))
    (let [art [[0 0 1 0 0 1 0 0]
               [0 0 1 0 0 1 0 0]
               [1 0 0 0 0 0 0 1]
               [0 1 0 0 0 0 1 0]
               [0 0 1 1 1 1 0 0]]]
      (is (= (art->graphic-columns art) [[0 0 1 0 0]
                                         [0 0 0 1 0]
                                         [1 1 0 0 1]
                                         [0 0 0 0 1]
                                         [0 0 0 0 1]
                                         [1 1 0 0 1]
                                         [0 0 0 1 0]
                                         [0 0 1 0 0]])))))

(deftest art->nonogram-test
  (testing "Art given as an array of arrays containing pixel data in 1s and 0s should result
            in map containing :rows and :columns of the pixel data in nonogram format"
    (is (= (art->nonogram [[1]]) {:rows [[1]] :columns [[1]]}))
    (is (= (art->nonogram [[1 1]]) {:rows [[2]] :columns [[1] [1]]}))
    (is (= (art->nonogram [[1 0] [1 1]]) {:rows [[1] [2]] :columns [[2] [1]]}))
    (let [art [[0 0 1 0 0 1 0 0]
               [0 0 1 0 0 1 0 0]
               [1 0 0 0 0 0 0 1]
               [0 1 0 0 0 0 1 0]
               [0 0 1 1 1 1 0 0]]]
      (is (= (art->nonogram art) {:rows [[1 1] [1 1] [1 1] [1 1] [4]]
                                  :columns [[1] [1] [2 1] [1] [1] [2 1] [1] [1]]})))
    (is (= (art->nonogram [[1 1] [1 1]]) {:rows [[2] [2]] :columns [[2] [2]]}))))

(deftest nonogram-column->string-test
  (testing "Prints a single nonogram column in a string format, if it has multiple values, those should be separated with a line break"
    (is (= (nonogram-column->string '(2)) "2"))
    (is (= (nonogram-column->string '(2 3)) "23"))))

(deftest nonogram-row->string-test
  (testing "Prints a single nonogram row in a string format, if it has multiple values, those should be flattened and followed by a line break"
    (is (= (nonogram-row->string '(2)) "2"))
    (is (= (nonogram-row->string '(2 3)) "23"))))

(deftest nonogram-rows->printtable-test
  (testing "Takes nonogram rows from a nonogram map and returns a vector of maps with
    the keyword row containing the nonogram values of each row"
    (is (= (nonogram-rows->printtable [[2]]) [{:row "2"}]))
    (is (= (nonogram-rows->printtable [[2 3] [4]]) [{:row "23"} {:row "4"}]))))

(deftest nonogram-columns->printtable-test
  (testing "Takes nonogram columns from a nonogram map and returns a vector of maps with
    a map with keyword column0 containing the first part, a map with keyword column1 the next
    and so on."
    (is (= (nonogram-columns->printtable [[2]]) {:column0 "2"}))
    (is (= (nonogram-columns->printtable [[2 3] [4]]) {:column0 "23" :column1 "4"}))))

(deftest join-printtable-rows-and-columns-test
  (testing "Takes printtable formatted rows and column and joins them into a single array so all
they keys are used as headers."
    (is (= (join-printtable-rows-and-columns 
      (nonogram-rows->printtable [[2]])
      (nonogram-columns->printtable [[2]]))
      [{:column0 "2"} {:row "2"}]))))

(deftest form-printtable-headers-test
  (testing "Takes printtable formatted rows and columns and detemines the needed headers from them."
    (is (= (form-printtable-headers [{:row "23"} {:row "4"}] {:column0 "23" :column1 "4"})
           [:row :column0 :column1]))))

(deftest draw-nonogram-test
  (testing "Draws given nonogram map with rows and columns into a table where the first column has the
            nonogram counts for each row and the first rows has the nonogram counts for each column."
    (is (= (draw-nonogram {:rows [[2] [2]] :columns [[2] [2]]}) " 2222"))
    (is (= (draw-nonogram {:rows [[2 3] [2]] :columns [[2] [2 2]]}) " 222232"))))
