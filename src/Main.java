
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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

    public static void main(String[] args) {
        Conexao conexao = new Conexao();
        Connection basededados = conexao.conectar();

        if (basededados != null) {
            try {
                Statement stm;
                ResultSet dadosBrutos;
                try (PrintWriter writer = new PrintWriter("output.csv", "UTF-8")) {
                    writer.println("\"Id da transacao\",\"Id da unidade\",\"Id do colaborador\",\"Data da compra\",\"Id do cliente\",\"Nome do cliente\",\"Sexo do cliente\",\"Telefone do cliente\",\"Telefone Adicional do cliente\",\"E-mail do cliente\",\"CPF do cliente\",\"valor compra\",\"Tipo de cartão fidelidade\"");
                    Arquivo arquivo = new Arquivo();
                    String sql = arquivo.lerArquivoSQL();
                    stm = (Statement) basededados.createStatement();
                    dadosBrutos = stm.executeQuery(sql);
                    while (dadosBrutos.next()) {
                        String idTransacao = dadosBrutos.getString(1);
                        String idCancelamento = dadosBrutos.getString(2);
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
                        
                        if (idCancelamento == null) {
                            idFilial = String.valueOf(Integer.valueOf(idFilial) + 10000);
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
                                + "\"" + valor + "\"";
                        
                        System.out.println(linha);
                        writer.println(linha);
                    }
                }
                dadosBrutos.close();
                stm.close();
                UploadObjectSingleOperation aws = new UploadObjectSingleOperation();
                boolean upload = aws.upload();
                if(upload){
                    System.out.println("Arquivo enviado para S3 com sucesso");
                }
            } catch (SQLException | FileNotFoundException | UnsupportedEncodingException ex) {
                Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

}
