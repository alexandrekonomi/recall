INSERT INTO usuarios (id, nome, email, senha, perfil, ativo, criado_em) VALUES
(gen_random_uuid(), 'Dr. Alexandre', 'medico@clinica.com', '$2a$10$kVnHpaw7FiH0KMbYz8mkruY0.b0cs0fC/gouo4KdINumOHwZwTZRC', 'MEDICO', true, NOW()),
(gen_random_uuid(), 'Secretária Ana', 'secretaria@clinica.com', '$2a$10$kVnHpaw7FiH0KMbYz8mkruY0.b0cs0fC/gouo4KdINumOHwZwTZRC', 'SECRETARIA', true, NOW());