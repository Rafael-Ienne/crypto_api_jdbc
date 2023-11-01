package com.example.demo.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.example.demo.entity.Coin;

@Repository
@EnableAutoConfiguration
public class CoinRepository {

	private JdbcTemplate jdbcTemplate;

	public CoinRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}
	
	/*Método para inserir um dado no banco de dados*/
	public Coin insert(Coin coin) {
		Object[] attr = new Object[] {
			coin.getName(),
			coin.getPrice(),
			coin.getQuantity(),
			coin.getDateTime()
		};
		jdbcTemplate.update("INSERT INTO coin "
				+ "(name, price, quantity, datetime) "
				+ "VALUES "
				+ "(?, ?, ?, ?)", attr);
		
		return coin;
	}
	
	/*Método para retornar a soma das quantias do banco de dados*/
	public List<Coin> getAll() {
		return jdbcTemplate.query("select name, sum(quantity) as quantity from "
				+ "coin group by name", new RowMapper<Coin>(){
					@Override
					public Coin mapRow(ResultSet rs, int rowNum)throws SQLException {
						
						Coin coin = new Coin();
						coin.setName(rs.getString("name"));
						coin.setQuantity(rs.getBigDecimal("quantity"));
						
						return coin;
						
					}
				});
	}
	
	/*Método para retornar um dado do banco de dados com base no nome*/
	public List<Coin> getByName(String name) {
		
		Object[] attr = new Object[] {name};
		
		return jdbcTemplate.query("select * from coin "
				+ "where name = ?", new RowMapper<Coin>(){
					@Override
					public Coin mapRow(ResultSet rs, int rowNum)throws SQLException {
						
						Coin coin = new Coin();
						coin.setId(rs.getInt("id"));
						coin.setName(rs.getString("name"));
						coin.setPrice(rs.getBigDecimal("price"));
						coin.setQuantity(rs.getBigDecimal("quantity"));
						coin.setDateTime(rs.getTimestamp("datetime"));
						
						return coin;
						
					}
				}, attr);
	}
	
	/*Método para deletar uma moeda no banco de dados*/
	public Integer delete(Integer id) {
		return jdbcTemplate.update("delete from coin where id = ?", id);
	}
	
	/*Método para realizar o update de uma moeda no banco de dados*/
	public Coin update(Coin coin) {
		
		Object[] attr = new Object[] {
				coin.getName(),
				coin.getPrice(),
				coin.getQuantity(),
				coin.getId()
		};
		
		jdbcTemplate.update("update coin set name = ?, price = ?, quantity = ? where id = ?", attr);
		
		return coin;
	}
	
}
