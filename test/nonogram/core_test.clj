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
    (is (= (nonogram-column->string [2]) "2"))
    (is (= (nonogram-column->string [2 3]) "2\n3"))))

(deftest nonogram-row->string-test
  (testing "Prints a single nonogram row in a string format, if it has multiple values, those should be flattened and followed by a line break"
    (is (= (nonogram-row->string [2]) "2\n"))
    (is (= (nonogram-row->string [2 3]) "23\n"))))

(deftest draw-nonogram-test
  (testing "Draws given nonogram map with rows and columns into a table where the first column has the
            nonogram counts for each row and the first rows has the nonogram counts for each column."
    (is (= (draw-nonogram {:rows [[2] [2]] :columns [[2] [2]]}) " 22\n2\n2\n"))
    (is (= (draw-nonogram {:rows [[2 3] [2]] :columns [[2] [2 2]]}) " 22\n2\n23\n2\n"))))
