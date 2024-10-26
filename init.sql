USE ruleengine;

CREATE TABLE IF NOT EXISTS rule (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  rule_string TEXT
);

INSERT INTO rule (id, rule_string) VALUES
  (1, '((age > 30 AND department = Sales) OR (age < 25 AND department = Marketing)) AND (salary > 50000 OR experience > 5)'),
  (2, '((age > 30 AND department = Marketing)) AND (salary > 20000 OR experience > 5)');
