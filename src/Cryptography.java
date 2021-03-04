
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;
import java.lang.String;

public class Cryptography {
    public static void main(String[] args) {
        System.out.println("Введите исходную строку:");
        Scanner scan = new Scanner(System.in);
        String source = scan.nextLine();
        source = source.trim();
        source = source.toLowerCase(Locale.ROOT);
        railFence(source);
        columnMethod(source);
        rotatingLattice(source);
        Vigenere(source);
    }

    private static void railFence(String source) {
        System.out.println("\n" + "Метод железнодорожной изгороди:" + "\n" + "Введите ключ (число):");
        Scanner scan = new Scanner(System.in);
        int key = scan.nextInt();
        String encryption = encryptRailFence(source, key);
        System.out.print("Шифр : " + encryption + "\n");
        System.out.print("Расшифровка : " + decryptRailFence(encryption, key) + "\n");
    }

    private static void columnMethod(String source) {
        System.out.println("\n" + "Столбцовый метод:" + "\n" + "Введите ключ (слово):");
        Scanner scan = new Scanner(System.in);
        String key = scan.nextLine();
        String encryption = encryptColumnMethod(source, key);
        System.out.print("Шифр : " + encryption + "\n");
        System.out.print("Расшифровка : " + decryptColumnMethod(encryption, key) + "\n");
    }

    private static void rotatingLattice(String source) {
        System.out.println("\n" + "Метод поварачивающейся решётки:"+ "\n" + "Введите ключ (чётный размер матрицы ):");
        Scanner scan = new Scanner(System.in);
        int n = scan.nextInt();
        if (n % 2 == 0 && n*n >= source.length()) {
            int trafaret[][] = new int[n][n];
            trafaret = trafaretInput(n);
            String encryption = encryptRotatingLattice(source, n, trafaret);
            System.out.print("Шифр : " + encryption + "\n");
            System.out.print("Расшифровка : " + decryptRotatingLattice(encryption, n, trafaret) + "\n");
        }
        else System.out.println("Такая матрица не подходит !");
    }

    private static int[][] trafaretInput(int n) {
        int matrixTrafaret[][] = new int[n][n];
        int tempTrafaret[][] = new int[n][n];
        Scanner scan = new Scanner(System.in);
        //инициализация матриц
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++) {
                matrixTrafaret[i][j] = 0 ;  //заполнение нулями
                tempTrafaret[i][j] = 0 ;
            }
        for (int k = 0; k < 4 ; k++) {    //заполнение матрицы с цифрами
            int number = 1 ;
            for (int i = 0; i < n / 2; i++)
                for (int j = 0; j < n / 2; j++) {
                    tempTrafaret[i][j] = number;
                    number++ ;
                }
            tempTrafaret = MatrixRotate(tempTrafaret);
        }

        for (int k = 0 ;k < (n/2)*(n/2) ; ) {
            System.out.println("Введите координаты ячейки-отверстия (0-" + (n-1) + ")");
            int x = scan.nextInt();
            int y = scan.nextInt() ;
            boolean no = false ;
            for (int i = 0; i < n; i++)
                for (int j = 0; j < n; j++) {
                    if (tempTrafaret[i][j] == tempTrafaret[x][y]) {
                        if (matrixTrafaret[i][j] == 1) {
                            no = true ;
                        }
                    }
                }
            if (!no) {
                k++;
                matrixTrafaret[x][y] = 1 ;
            }
        }
        return matrixTrafaret ;
    }

    private static void Vigenere(String source) {
        System.out.println("\n" + "Шифр Вижинера:" + "\n" + "Введите ключ (слово):");
        Scanner scan = new Scanner(System.in);
        String key = scan.nextLine();
        String encryption = encryptVigenere(source, key);
        System.out.print("Шифр : " + encryption + "\n");
        System.out.print("Расшифровка : " + decryptVigenere(encryption, key) + "\n");
    }

    private static String encryptRailFence(String source, int key) {
        if (key < 1) return null;
        if (key == 1) return source;
        String result = "";
        char[][] matrix = new char[key][(source.length())];
        boolean dir_down = false;
        int row = 0, col = 0;

        for (int i = 0; i < key; i++) {
            for (int j = 0; j < source.length(); j++)
                matrix[i][j] = '\n';
        }

        for (int i = 0; i < source.length(); i++) {
            if (row == 0 || row == key - 1)
                dir_down = !dir_down;

            matrix[row][col++] = source.charAt(i);

            if (dir_down) row++;
            else row--;
        }
        for (int i = 0; i < key; i++)
            for (int j = 0; j < source.length(); j++)
                if (matrix[i][j] != '\n')
                    result += matrix[i][j];
        return result;
    }

    private static String decryptRailFence(String source, int key) {
        if (key < 1) return null;
        if (key == 1) return source;
        String result = "";
        boolean dir_down = false;
        int row = 0, col = 0, index = 0;
        char matrix[][] = new char[key][source.length()];
        for (int i = 0; i < key; i++) {
            for (int j = 0; j < source.length(); j++)
                matrix[i][j] = '\n';
        }
        for (int i = 0; i < source.length(); i++) {
            if (row == 0) dir_down = true;
            if (row == key - 1) dir_down = false;
            matrix[row][col++] = '*';
            if (dir_down) row++;
            else row--;
        }
        for (int i = 0; i < key; i++)
            for (int j = 0; j < source.length(); j++)
                if (matrix[i][j] == '*' && index < source.length())
                    matrix[i][j] = source.charAt(index++);
        row = 0;
        col = 0;
        for (int i = 0; i < source.length(); i++) {
            if (row == 0)
                dir_down = true;
            if (row == key - 1)
                dir_down = false;

            if (matrix[row][col] != '*')
                result += matrix[row][col++];

            // find the next row using direction flag
            if (dir_down) row++;
            else row--;
        }
        return result;
    }

    private static String encryptColumnMethod(String source, String key) {
        String result = "";
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int weidth = key.length();
        int hidth = source.length() / key.length();

        if (source.length() % key.length() == 0) {
            hidth = 1 + hidth;
        } else {
            hidth = 2 + hidth;
        }
        char matrix[][] = new char[hidth][weidth];

        for (int i = 0; i < weidth; i++) {
            matrix[0][i] = key.charAt(i);
        }

        int index = 0;
        for (int i = 1; i < hidth; i++) {
            for (int j = 0; j < weidth; j++) {
                if (index < source.length()) {
                    matrix[i][j] = source.charAt(index);
                    index++;
                } else {
                    matrix[i][j] = ' ';
                }
            }
        }

        for (int i = 0; i < alphabet.length(); i++) {
            int indexOf = key.indexOf(alphabet.charAt(i));
            while (indexOf != -1) {
                for (int k = 1; k < hidth; k++) {
                    result = result + matrix[k][indexOf];
                }
                if (indexOf < key.length() - 1) {
                    indexOf = key.indexOf(alphabet.charAt(i), indexOf + 1);
                } else {
                    indexOf = -1;
                }
            }
        }

        return result;
    }

    private static String decryptColumnMethod(String source, String key) {
        String result = "";
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int weidth = key.length();
        int hidth = source.length() / key.length();

        if (source.length() % key.length() == 0) {
            hidth = 1 + hidth;
        } else {
            hidth = 2 + hidth;
        }
        char matrix[][] = new char[hidth][weidth];

        for (int i = 0; i < weidth; i++) {
            matrix[0][i] = key.charAt(i);
        }

        int index = 0;
        for (int i = 0; i < alphabet.length(); i++) {
            int indexOf = key.indexOf(alphabet.charAt(i));
            while (indexOf != -1) {
                for (int k = 1; k < hidth; k++) {
                    matrix[k][indexOf] = source.charAt(index);
                    index++;
                }
                if (indexOf < key.length() - 1) {
                    indexOf = key.indexOf(alphabet.charAt(i), indexOf + 1);
                } else {
                    indexOf = -1;
                }
            }
        }

        for (int i = 1; i < hidth; i++) {
            for (int j = 0; j < weidth; j++) {
                result = result + matrix[i][j];
            }
        }
        return result;
    }

    private static String encryptRotatingLattice(String source, int n, int[][] trafaret) {
        String result = "";
        char matrixEncrypt[][] = new char[n][n];

        for (int i = 0; i < n ; i++)
            for(int j = 0 ; j < n ; j++)
                matrixEncrypt[i][j] = (char) ((int)(Math.random() * 26) + 'a') ;

        int index = 0, rotatingNumber = 0;

        for ( rotatingNumber = 0 ; rotatingNumber < 4 ;rotatingNumber++){
            if (index <= source.length()) {
                for (int i = 0; i < n; i++)
                    for (int j = 0; j < n; j++) {
                        if (trafaret[i][j] == 1) {
                            if (index < source.length()) {
                                matrixEncrypt[i][j] = source.charAt(index);
                            }
                            if (index == source.length()) {
                                matrixEncrypt[i][j] = '0';
                            }
                            index++ ;
                        }
                    }
            }
            matrixEncrypt = MatrixRotate(matrixEncrypt);
        }
         for (int i = 0 ; i < n; i++)
             for (int j = 0 ; j < n; j++)
                 result = result + matrixEncrypt[i][j];

        return result;
    }

    private static char[][] MatrixRotate (char[][] matrix){
        int SIDE = matrix.length;
        char[][] result = new char[SIDE][SIDE];

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                result[i][j] = matrix[SIDE - j - 1][i];
            }
        }
        return result;
    }
    private static int[][] MatrixRotate (int[][] matrix) {
        int SIDE = matrix.length;
        int[][] result = new int[SIDE][SIDE];

        for (int i = 0; i < SIDE; i++) {
            for (int j = 0; j < SIDE; j++) {
                result[i][j] = matrix[SIDE - j - 1][i];
            }
        }
        return result;
    }

    private static String decryptRotatingLattice(String source, int n, int[][] trafaret) {
        String result = "";
        char matrixEncrypt[][] = new char[n][n];

        int index = 0;
        for (int i = 0; i < n; i++)         //заполнение таблицы
            for (int j = 0; j < n; j++) {
                matrixEncrypt[i][j] = source.charAt(index);
                index++;
            }

        int rotatingNumber;
        for ( rotatingNumber = 0 ; rotatingNumber < 4 ;rotatingNumber++){
            for (int i = 0; i < n; i++)
                   for (int j = 0; j < n; j++) {
                       if (trafaret[i][j] == 1) {
                           result = result + matrixEncrypt[i][j];
                       }
                   }
            matrixEncrypt = MatrixRotate(matrixEncrypt);
        }
        return result.substring(0,result.indexOf('0'));
    }



    private static String encryptVigenere(String source, String key) {
        String result = "";
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        final int LETTERS_COUNT = 26;

        char vigenereMatrix[][] = new char[LETTERS_COUNT][LETTERS_COUNT];

        for (int j = 0; j < LETTERS_COUNT; j++) {           // заполнение таблицы Вижинера
            vigenereMatrix[0][j] = alphabet.charAt(j);
        }
        for (int i = 1; i < LETTERS_COUNT; i++) {
            for (int j = 0; j < LETTERS_COUNT - i; j++) {
                vigenereMatrix[i][j] = vigenereMatrix[i - 1][j + 1];
            }
            int index = 0;
            for (int j = LETTERS_COUNT - i; j < LETTERS_COUNT; j++) {
                vigenereMatrix[i][j] = alphabet.charAt(index);
                index++;
            }
        }

        int i = 0, j;
        while (i < key.length() && i < source.length()) {
            result = result + vigenereMatrix[source.charAt(i) - 'a'][key.charAt(i) - 'a'];
            i++;
        }
        j = 0;
        while (i < source.length()) {
            result = result + vigenereMatrix[source.charAt(i) - 'a'][source.charAt(j) - 'a'];
            i++;
            j++;
        }

        return result;
    }

    private static String decryptVigenere(String source, String key) {
        String result = "";
        final String alphabet = "abcdefghijklmnopqrstuvwxyz";
        final int LETTERS_COUNT = 26;

        char vigenereMatrix[][] = new char[LETTERS_COUNT][LETTERS_COUNT];

        for (int j = 0; j < LETTERS_COUNT; j++) {           // заполнение таблицы Вижинера
            vigenereMatrix[0][j] = alphabet.charAt(j);
        }
        for (int i = 1; i < LETTERS_COUNT; i++) {
            for (int j = 0; j < LETTERS_COUNT - i; j++) {
                vigenereMatrix[i][j] = vigenereMatrix[i - 1][j + 1];
            }
            int index = 0;
            for (int j = LETTERS_COUNT - i; j < LETTERS_COUNT; j++) {
                vigenereMatrix[i][j] = alphabet.charAt(index);
                index++;
            }
        }

        int i = 0;
        while (i < key.length() && i < source.length()) {
            int j = 0;
            while (vigenereMatrix[j][key.charAt(i) - 'a'] != source.charAt(i)) {
                j++;
            }
            result = result + (char) ('a' + j);
            i++;
        }
        int index = 0;
        while (i < source.length()) {
            int j = 0;
            while (vigenereMatrix[j][result.charAt(index) - 'a'] != source.charAt(i)) {
                j++;
            }
            result = result + (char) ('a' + j);
            i++;
            index++;
        }
        return result;
    }
}

