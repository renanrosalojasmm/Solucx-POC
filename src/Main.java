
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author renan.rosa
 */
public class Main {

    public static void main(String[] args) throws IOException {
        Conexao conexao = new Conexao();
        Connection basededados = conexao.conectar();
        String filename = "";
        String[] arquivos = {"7dias.sql", "mesmo_dia.sql"};
        ArrayList<String> ids = new ArrayList<>();

        ArrayList<String> lerArquivosComTransacoesAnteriores = lerArquivosComTransacoesAnteriores();

        if (basededados != null) {
            try {
                Statement stm = null;
                ResultSet dadosBrutos = null;
                int year = Calendar.getInstance().get(Calendar.YEAR);
                int monthI = Calendar.getInstance().get(Calendar.MONTH) + 1;
                int dayI = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);

                String month = String.valueOf(monthI);
                String day = String.valueOf(dayI);

                if (monthI < 10) {
                    month = "0" + month;
                }

                if (dayI < 10) {
                    day = "0" + day;
                }

                filename = String.valueOf(year) + String.valueOf(month) + String.valueOf(day) + "_transacoes.csv";
                try (PrintWriter writer = new PrintWriter(filename, "UTF-8")) {
                    writer.println("\"Id da transacao\",\"Id da unidade\",\"Id do colaborador\",\"Data da compra\",\"Id do cliente\",\"Nome do cliente\",\"Sexo do cliente\",\"Telefone do cliente\",\"Telefone Adicional do cliente\",\"E-mail do cliente\",\"CPF do cliente\",\"valor compra\",\"Entregar\",\"Montagem\"");
                    Arquivo arquivo = new Arquivo();
                    for (String arquivo1 : arquivos) {
                        System.out.println("Processando " + arquivo1);
                        String sql = arquivo.lerArquivoSQL(arquivo1);
                        stm = (Statement) basededados.createStatement();
                        dadosBrutos = stm.executeQuery(sql);
                        while (dadosBrutos.next()) {
                            String idTransacao = dadosBrutos.getString(1);
                            if (!ids.contains(idTransacao)) {
                                ids.add(idTransacao);
                                String idCPNJCPF = dadosBrutos.getString(2);
                                String idFilial = dadosBrutos.getString(3);
                                String idColaborador = dadosBrutos.getString(4);
                                String dia = dadosBrutos.getString(5);
                                String idCliente = dadosBrutos.getString(6);
                                String nome = dadosBrutos.getString(7);
                                String sexo = dadosBrutos.getString(8);
                                String telefone = dadosBrutos.getString(9);
                                String telefoneAdicional = dadosBrutos.getString(10);
                                String email = dadosBrutos.getString(11);
                                String cpf = dadosBrutos.getString(12);
                                String valor = String.valueOf(dadosBrutos.getFloat(13));
                                String entregar = dadosBrutos.getString(14);
                                String montagem = dadosBrutos.getString(15);

                                if (entregar.equals("0")) {
                                    entregar = "Nao";
                                } else {
                                    entregar = "Sim";
                                }

                                if (montagem.equals("0")) {
                                    montagem = "Nao";
                                } else {
                                    montagem = "Sim";
                                }

                                String linha = "\"" + idTransacao + "\","
                                        + "\"" + idFilial + "\","
                                        + "\"" + idColaborador + "\","
                                        + "\"" + dia + "\","
                                        + "\"" + idCliente + "\","
                                        + "\"" + nome + "\","
                                        + "\"" + sexo + "\","
                                        + "\"" + telefone + "\","
                                        + "\"" + telefoneAdicional + "\","
                                        + "\"" + email + "\","
                                        + "\"" + cpf + "\","
                                        + "\"" + valor + "\","
                                        + "\"" + entregar + "\","
                                        + "\"" + montagem + "\"";

                                System.out.println(linha);

                                String tran = idTransacao + "," + idFilial;
                                if (!lerArquivosComTransacoesAnteriores.contains(tran)) {
                                    writer.println(linha);
                                    lerArquivosComTransacoesAnteriores.add(tran);
                                }
                                
                            }
                        }
                    }
                }
                salvarArquivosComTransacoesAnteriores(lerArquivosComTransacoesAnteriores);
                dadosBrutos.close();
                stm.close();
                UploadObjectSingleOperation aws = new UploadObjectSingleOperation(filename);
                boolean upload = aws.upload();
                if (upload) {
                    System.out.println("Arquivo enviado para S3 com sucesso");
                }
            } catch (SQLException | FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private static ArrayList<String> lerArquivosComTransacoesAnteriores() throws FileNotFoundException, IOException {

        FileReader fileReader = new FileReader("transacoes enviadas.txt");

        BufferedReader bufferedReader = new BufferedReader(fileReader);
        ArrayList<String> lines = new ArrayList<String>();
        String line = null;

        while ((line = bufferedReader.readLine()) != null) {
            lines.add(line);
        }

        bufferedReader.close();

        return lines;
    }

    private static void salvarArquivosComTransacoesAnteriores(ArrayList<String> lista) throws FileNotFoundException {
        PrintWriter pw = new PrintWriter(new FileOutputStream("transacoes enviadas.txt", false));
        for (String valor : lista) {
            pw.println(valor);
        }
        pw.close();
    }
}
