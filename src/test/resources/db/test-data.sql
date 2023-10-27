-- Inserções de dados na tabela public.addressmodel (Inserção 1)
INSERT INTO public.addressmodel (finalshippingcost, riskarea, deliverydate, addressid, address1, address2, city, country, firstname, lastname, phonenumber, postacode, uf, userid)
VALUES (10.0, true, '2023-10-30 14:30:00', '9a5c9b4a-3b88-4a37-94c8-634c25de87e1', '123 Main St', 'Apt 4B', 'New York', 'USA', 'John', 'Doe', '+1 123-456-7890', '10001', 'NY', 'userId');

-- Inserções de dados na tabela public.addressmodel (Inserção 2)
INSERT INTO public.addressmodel (finalshippingcost, riskarea, deliverydate, addressid, address1, address2, city, country, firstname, lastname, phonenumber, postacode, uf, userid)
VALUES (8.5, false, '2023-11-15 13:45:00', 'b7431e27-38a9-4b8d-87ae-85cda2f106d5', '456 Elm St', NULL, 'Los Angeles', 'USA', 'Jane', 'Smith', '+1 987-654-3210', '90002', 'CA', 'userId');

-- Inserções de dados na tabela public.shippingmodel (Inserção 1)
INSERT INTO public.shippingmodel (shippingmethod, state, address_model_id, shippingid, carriercode, orderid, trackingurl, userid)
VALUES (1, 4, '9a5c9b4a-3b88-4a37-94c8-634c25de87e1', '57c54c33-88b3-4e99-bb09-5dbf91e6e186', 'UPS', 'orderId', 'https://www.example.com/tracking/123', 'userId');

-- Inserções de dados na tabela public.shippingmodel (Inserção 2)
INSERT INTO public.shippingmodel (shippingmethod, state, address_model_id, shippingid, carriercode, orderid, trackingurl, userid)
VALUES (0, 2, '9a5c9b4a-3b88-4a37-94c8-634c25de87e1', 'c6fca2e5-178c-488e-aa7e-cb276c1b7ce9', 'FedEx', 'orderId', 'https://www.example.com/tracking/456', 'userId');