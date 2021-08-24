insert into cozinha (id , nome) values (1 , 'Tailandesa');
insert into cozinha (id , nome) values (2 , 'Indiana');
insert into cozinha (id , nome) values (3 , 'Brasileira');

insert into estado (id, nome) values (1, 'Minas Gerais');
insert into estado (id, nome) values (2, 'São Paulo');
insert into estado (id, nome) values (3, 'Ceará');

insert into cidade (id, nome, estado_id) values (1, 'Uberlândia', 1);
insert into cidade (id, nome, estado_id) values (2, 'Belo Horizonte', 1);
insert into cidade (id, nome, estado_id) values (3, 'São Paulo', 2);
insert into cidade (id, nome, estado_id) values (4, 'Campinas', 2);
insert into cidade (id, nome, estado_id) values (5, 'Fortaleza', 3);

insert into restaurante (nome , taxa_frete , cozinha_id , data_cadastro, data_atualizacao, endereco_cidade_id, endereco_cep, endereco_logradouro, endereco_numero, endereco_bairro) values ('Outback' , 10 , 1 ,utc_timestamp, utc_timestamp, 1,'38400-999', 'Rua João Pinheiro', '1000', 'Centro');
insert into restaurante (nome , taxa_frete , cozinha_id , data_cadastro, data_atualizacao) values ('Gran parrilha' , 9.50 , 2 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Fogo de chao' , 15 , 3 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Buxixo' , 0 , 1 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Gratini' , 9.50 , 2 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Caçador' , 13 , 3 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Vizinhando' , 11 , 1 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Pavão azul' , 4 , 2 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Bar do momo' , 7 , 3 ,utc_timestamp, utc_timestamp);
insert into restaurante (nome , taxa_frete , cozinha_id, data_cadastro, data_atualizacao) values ('Bode cheiroso' , 0 , 1 ,utc_timestamp, utc_timestamp);

insert into forma_pagamento (descricao) values ('Dinheiro');
insert into forma_pagamento (descricao) values ('Catão de crédito');
insert into forma_pagamento (descricao) values ('Cartão de débito');
insert into forma_pagamento (descricao) values ('Boleto');



insert into permissao (nome , descricao) values ('Adm' , 'Administrador');
insert into permissao (nome , descricao) values ('Gerente' , 'Gerente nivel 1');
insert into permissao (nome , descricao) values ('Sup' , 'Supervisor');
insert into permissao (nome , descricao) values ('Fun' , 'Funcionario');

insert into restaurante_forma_pagamento(restaurante_id , forma_pagamento_id) values (1, 1), (1, 2), (1, 3), (2, 3), (3, 2), (3, 3);