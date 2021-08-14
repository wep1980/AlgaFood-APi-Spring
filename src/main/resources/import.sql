insert into cozinha (id , nome) values (1 , 'Tailandesa');
insert into cozinha (id , nome) values (2 , 'Indiana');
insert into cozinha (id , nome) values (3 , 'Brasileira');

insert into restaurante (nome , taxa_frete , cozinha_id) values ('Paris 6' , 10 , 1);
insert into restaurante (nome , taxa_frete , cozinha_id) values ('Gran parrilha' , 9.50 , 2);
insert into restaurante (nome , taxa_frete , cozinha_id) values ('Fogo de chao' , 15 , 3);

insert into forma_pagamento (descricao) values ('Dinheiro');
insert into forma_pagamento (descricao) values ('Catão de crédito');
insert into forma_pagamento (descricao) values ('Cartão de débito');
insert into forma_pagamento (descricao) values ('Boleto');


insert into estado (id , nome) values (1 , 'Rio de janeiro');
insert into estado (id , nome) values (2 , 'Rio grande do sul');
insert into estado (id , nome) values (3 , 'Bahia');

insert into cidade (nome , estado_id) values ('Rio de janeiro' , 1);
insert into cidade (nome , estado_id) values ('Porto alegre' , 2);
insert into cidade (nome , estado_id) values ('Salvador' , 3);

insert into permissao (nome , descricao) values ('Adm' , 'Administrador');
insert into permissao (nome , descricao) values ('Gerente' , 'Gerente nivel 1');
insert into permissao (nome , descricao) values ('Sup' , 'Supervisor');
insert into permissao (nome , descricao) values ('Fun' , 'Funcionario');