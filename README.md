# Order Service
A mock microservice to handle orders within an ecommerce platform.

This is designed to be an "internal" service not exposed to the public so no customer or product validation is done (as it's assumed the validation would happen on the actual backend prior to interacting with this service.)

## What this service does
* Add/remove/edit products in a cart
  * These are orders with a STAGED status
* Place order (moves STAGED order to PLACED status)
* Cancel order (moves PLACED order to CANCELLED status)
* Deliver order (moves PLACED order to DELIVERED)
* View cart or orders

## Dev Setup
For first time setup, create a postgres user `osroot` and use:
* `create database orderservice owner 'osroot';`