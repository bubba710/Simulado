package main.java;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GeradorPessoasComEndereco {

    public static void main(String[] args) {
        String pessoasFile = "src/main/resources/Pessoas.csv";
        String enderecosFile = "src/main/resources/Enderecos.csv";
        String pessoasComEnderecoFile = "src/main/resources/PessoasComEndereco.csv";

        List<Pessoa> pessoas = lerPessoas(pessoasFile);
        Map<Integer, List<Endereco>> mapEnderecos = lerEnderecos(enderecosFile);

        gerarPessoasComEndereco(pessoas, mapEnderecos, pessoasComEnderecoFile);

        System.out.println("Arquivo PessoasComEndereco.csv gerado com sucesso!");
    }

    private static List<Pessoa> lerPessoas(String pessoasFile) {
        List<Pessoa> pessoas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(pessoasFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(", ");
                int codigo = Integer.parseInt(partes[0]);
                String nome = partes[1];
                pessoas.add(new Pessoa(codigo, nome));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pessoas;
    }

    private static Map<Integer, List<Endereco>> lerEnderecos(String enderecosFile) {
        Map<Integer, List<Endereco>> mapEnderecos = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(enderecosFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] partes = line.split(", ");
                String rua = partes[0];
                String cidade = partes[1];
                int codigoPessoa = Integer.parseInt(partes[2]);
                Endereco endereco = new Endereco(rua, cidade);
                if (!mapEnderecos.containsKey(codigoPessoa)) {
                    mapEnderecos.put(codigoPessoa, new ArrayList<>());
                }
                mapEnderecos.get(codigoPessoa).add(endereco);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return mapEnderecos;
    }

    private static void gerarPessoasComEndereco(List<Pessoa> pessoas, Map<Integer, List<Endereco>> mapEnderecos, String outputFile) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
            for (Pessoa pessoa : pessoas) {
                int codigo = pessoa.getCodigo();
                String nome = pessoa.getNome();
                if (mapEnderecos.containsKey(codigo)) {
                    List<Endereco> enderecos = mapEnderecos.get(codigo);
                    for (Endereco endereco : enderecos) {
                        String linha = String.format("%d,%s,%s,%s\n", codigo, nome, endereco.getRua(), endereco.getCidade());
                        writer.write(linha);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
