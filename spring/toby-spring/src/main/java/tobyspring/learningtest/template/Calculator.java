package tobyspring.learningtest.template;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Calculator {
    public Integer calcSum(String filepath) throws IOException {
        // BufferedReaderCallback sumCallback = new BufferedReaderCallback() {
        //     public Integer doSomethingWithReader(BufferedReader br) throws IOException {
        //         int sum = 0;
        //         String line = null;
    
        //         while ( (line = br.readLine()) != null ) {
        //             sum += Integer.parseInt(line);
        //         }
    
        //         return sum;
        //     }
        // };
    
        // return fileReadTemplate(filepath, sumCallback);
        
        LineCallback<Integer> callback = new LineCallback<Integer>() {
            public Integer doSomethingWithLine(String line, Integer value) {
                return value += Integer.valueOf(line);
            } 
        };

        return lineReadTemplate(filepath, callback, 0);
    }

    public Integer calcMultiple(String filepath) throws IOException {
        // BufferedReaderCallback multipleCallback = new BufferedReaderCallback() {
        //     public Integer doSomethingWithReader(BufferedReader br) throws IOException {
        //         int multiple = 1;
        //         String line = null;

        //         while ( (line = br.readLine()) != null ) {
        //             multiple *= Integer.parseInt(line);
        //         }

        //         return multiple;
        //     }
        // };

        // return fileReadTemplate(filepath, multipleCallback);

        LineCallback<Integer> callback = new LineCallback<Integer>() {
            public Integer doSomethingWithLine(String line, Integer value) {
                return value *= Integer.valueOf(line);
            }
        };

        return lineReadTemplate(filepath, callback, 1);
    }

    public String concatenate(String filepath) throws IOException {
        LineCallback<String> callback = new LineCallback<String>() {
            public String doSomethingWithLine(String line, String value) {
                return value + line;
            }
        };

        return lineReadTemplate(filepath, callback, "");
    }

    public Integer fileReadTemplate(String filepath, BufferedReaderCallback callback) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));

            int res = callback.doSomethingWithReader(br);

            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public <T> T lineReadTemplate(String filepath, LineCallback<T> callback, T initVal) throws IOException {
        BufferedReader br = null;

        try {
            br = new BufferedReader(new FileReader(filepath));
            
            T res = initVal;
            String line = null;

            while( (line = br.readLine()) != null ) {
                res = callback.doSomethingWithLine(line, res);
            }

            return res;
        } catch (IOException e) {
            System.out.println(e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println(e.getMessage());
                }
            }
        }


    }
}
