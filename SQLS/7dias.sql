SELECT DISTINCT pv.idpedidovenda as "Id da transacao", 
       pv.idcnpj_cpf,
       pv.idfilial as "Id da unidade", 
       pv.idvendedor as "Id do colaborador", 
       pv.datainclusao as "Data da compra", 
       pv.idcnpj_cpf as "Id do cliente",
       pv.nome as "Nome do cliente", 
       substring(ts.descricao from 1 for 1) as "Sexo do cliente", 
       t.ddd || t.numero as "Telefone do cliente", 
       '' as "Telefone Adicional do cliente",
       pa.email as "E-mail do cliente",
       p.cnpj_cpf as "CPF do cliente",
       pv.totalrealizadoavista::money::numeric::float8 as "valor compra",
       pv.entregar as "Entregar", 
       pr.possuimontagem as "Montagem"
  FROM rst.pedidovenda pv
  join glb.referenciapessoal rf on pv.idcnpj_cpf = rf.idcnpj_cpf 
  join glb.pessoa p on pv.idcnpj_cpf = p.idcnpj_cpf 
  join glb.pessoaauxiliar pa on pv.idcnpj_cpf = pa.idcnpj_cpf
  join sis.tiposexo ts on ts.idtiposexo = p.idtiposexo
  join glb.telefone t on t.idcnpj_cpf = p.idcnpj_cpf
  join rst.itembase ib on ib.idpedidovenda = pv.idpedidovenda and ib.idfilial = pv.idfilial
  join glb.produto pr on pr.idproduto = ib.idproduto
  where	
	pv.idsituacaopedidovenda in (4) and
	pv.nome not like '' and
	pv.datainclusao between now() - interval '7 days' and now() and
	pv.idfilial > 10001 and
	t.numero not like '' and
	t.numero not like '3%'-- and
  --t.idtipotelefone = 2