package org.example;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static void main(String[] args) {
        EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("example-unit");

        EntityManager entityManager = entityManagerFactory.createEntityManager();
        System.out.println("Estamos en Marcha");

        try {
           entityManager.getTransaction().begin();

            //Creamos el cliente
            Cliente cliente1 = Cliente.builder()
                    .nombre("Ricardo")
                    .apellido("Perez")
                    .dni(32987255)
                    .build();
            //Creamos el domicilio
            Domicilio domicilio1= Domicilio.builder()
                    .nombreCalle("Alpidio Gonzalez")
                    .numero(1503)
                    .build();

            //Asignamos el domicilio al cliente (bidireccional)
            cliente1.setDomicilio(domicilio1);
            domicilio1.setCliente(cliente1);

            //Creamos las categorias
            Categoria perecederos= Categoria.builder()
                    .denominacion("Perecederos")
                    .build();
            Categoria bebidas= Categoria.builder()
                    .denominacion("Bebidas")
                    .build();
            Categoria limpieza= Categoria.builder()
                    .denominacion("Limpieza")
                    .build();

            //Creamos los articulos
            Articulo articulo1= Articulo.builder()
                    .denominacion("Fideos Tirabuzon")
                    .cantidad(100)
                    .precio(700)
                    .build();

            Articulo articulo2= Articulo.builder()
                    .denominacion("Coca-Cola")
                    .cantidad(200)
                    .precio(1500)
                    .build();
            //Creamos la factura 1
            Factura factura1= Factura.builder()
                    .numero(100)
                    .fecha("23/03/2024")
                    .cliente(cliente1)
                    .build();

            // Ahora al art1 le asignamos las categorias
            articulo1.getCategorias().add(perecederos);


            // Ahora a las categorias le asignamos el art1
            perecederos.getArticulos().add(articulo1);

            // Ahora al art2 le asignamos la categoria
            articulo2.getCategorias().add(bebidas);
            // Ahora a las categorias le asignamos el art2
            bebidas.getArticulos().add(articulo2);

            //Creamos detalles facturas
            DetalleFactura detalle1= DetalleFactura.builder()
                    .cantidad(4)
                    .articulo(articulo1)
                    .build();
            detalle1.setSubtotal(detalle1.getCantidad()*detalle1.getArticulo().getPrecio());
            articulo1.getDetalle().add(detalle1);
            factura1.getDetalle().add(detalle1);
            detalle1.setFactura(factura1);

            //Creamos otro detalle de factura
            DetalleFactura detalle2= DetalleFactura.builder()
                    .cantidad(1)
                    .articulo(articulo2)
                    .build();
            detalle2.setSubtotal(detalle2.getCantidad()*detalle2.getArticulo().getPrecio());
            articulo2.getDetalle().add(detalle2);
            factura1.getDetalle().add(detalle2);
            detalle2.setFactura(factura1);

            factura1.setTotal(detalle1.getSubtotal()+ detalle2.getSubtotal());

            entityManager.persist(factura1);

            //Creamos el cliente
            Cliente cliente2 = Cliente.builder()
                    .nombre("Ignacio")
                    .apellido("Sanchez")
                    .dni(42750538)
                    .build();

            //Creamos el domicilio
            Domicilio domicilio2= Domicilio.builder()
                    .nombreCalle("Artigas")
                    .numero(456)
                    .build();

            //Asignamos el domicilio al cliente (bidireccional)
            cliente2.setDomicilio(domicilio2);
            domicilio2.setCliente(cliente2);

            //Creamos los articulos
            Articulo articulo3= Articulo.builder()
                    .denominacion("Queso en barra")
                    .cantidad(400)
                    .precio(2500)
                    .build();
            //Cremaos otra factura
            Factura factura2= Factura.builder()
                    .numero(101)
                    .fecha("05/05/2024")
                    .cliente(cliente2)
                    .build();
            // Ahora al art3 le asignamos las categorias
            articulo3.getCategorias().add(perecederos);
            articulo3.getCategorias().add(bebidas);
            // Ahora a las categorias le asignamos el art3
            bebidas.getArticulos().add(articulo3);
            perecederos.getArticulos().add(articulo3);


            //Creamos un detalle factura
            DetalleFactura detalle3= DetalleFactura.builder()
                    .cantidad(2)
                    .articulo(articulo3)
                    .build();
            detalle3.setSubtotal(detalle3.getCantidad()*detalle3.getArticulo().getPrecio());

            articulo3.getDetalle().add(detalle3);
            factura2.getDetalle().add(detalle3);
            detalle3.setFactura(factura2);

            //Creamos otro detalle factura
            DetalleFactura detalle4= DetalleFactura.builder()
                    .cantidad(1)
                    .articulo(articulo1)
                    .build();
            detalle4.setSubtotal(detalle4.getCantidad()*detalle4.getArticulo().getPrecio());
            articulo1.getDetalle().add(detalle4);
            factura2.getDetalle().add(detalle4);
            detalle4.setFactura(factura2);

            factura2.setTotal(detalle3.getSubtotal()+ detalle4.getSubtotal());
            entityManager.persist(factura2);

            entityManager.flush();
            entityManager.getTransaction().commit();


            //Actualizar la factura
            entityManager.getTransaction().begin();
            Factura factura3 = entityManager.find(Factura.class, 1L);
            factura3.setNumero(35);
            entityManager.merge(factura3);
            entityManager.getTransaction().commit();
            System.out.println("La factura ha sido actualizada exitosamente "+factura3);


           /* // Eliminar la factura
            entityManager.getTransaction().begin();
            Factura factura4 = entityManager.find(Factura.class, 1L);
            entityManager.remove(factura4);
            entityManager.getTransaction().commit();
            System.out.println("La factura ha sido eliminada exitosamente "+factura4);*/



        }catch (Exception e){
            entityManager.getTransaction().rollback();
            System.out.println(e.getMessage());
            System.out.println("No se pudo grabar la clase factura");
        }

        // Cerrar el EntityManager y el EntityManagerFactory
        entityManager.close();
        entityManagerFactory.close();


    }

}