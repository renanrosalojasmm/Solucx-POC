(SELECT DISTINCT pv.idpedidovenda as "Id da transacao", c.idcancelamento, pv.idfilial as "Id da unidade", '' as "Id do colaborador", pv.datainclusao as "Data da compra", pv.idcnpj_cpf as "Id do cliente", pv.nome as "Nome do cliente", substring(ts.descricao from 1 for 1) as "Sexo do cliente", t.ddd || t.numero as "Telefone do cliente",  '' as "Telefone Adicional do cliente",pa.email as "E-mail do cliente", p.cnpj_cpf as "CPF do cliente", totalpedido::money::numeric::float8 as "valor compra"
  FROM rst.pedidovenda pv
  join glb.referenciapessoal rf on pv.idcnpj_cpf = rf.idcnpj_cpf 
  join glb.pessoa p on pv.idcnpj_cpf = p.idcnpj_cpf 
  join glb.pessoaauxiliar pa on pv.idcnpj_cpf = pa.idcnpj_cpf
  join sis.tiposexo ts on ts.idtiposexo = p.idtiposexo
  join glb.telefone t on t.idcnpj_cpf = p.idcnpj_cpf
  left join vnd.pedidovenda vndpv on pv.idpedidovenda = vndpv.idpedidovenda and pv.idfilial = vndpv.idfilial
  left join vnd.cancelamento c on vndpv.idpedido = c.idpedido
  left join vnd.cancelamento_itemproduto it on it.idcancelamento = c.idcancelamento and idmotivodevolucao = 13
  where	
	pv.nome not like '' and
  pv.datainclusao between now() - interval '7 days' and now() - interval '1 day' and
	pv.idfilial > 10001 and
	t.numero not like '' and
	t.numero not like '3%'
  limit 2)