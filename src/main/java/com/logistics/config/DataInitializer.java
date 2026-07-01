package com.logistics.config;

import com.logistics.entity.*;
import com.logistics.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final RoleRepository roleRepo;
    private final UserRepository userRepo;
    private final SupplierRepository supplierRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final WarehouseRepository warehouseRepo;
    private final InventoryRepository inventoryRepo;
    private final CustomerRepository customerRepo;
    private final CustomerOrderRepository orderRepo;
    private final ShipmentRepository shipmentRepo;
    private final VehicleRepository vehicleRepo;
    private final RouteRepository routeRepo;
    private final StockMovementRepository stockMovementRepo;
    private final PasswordEncoder passwordEncoder;
    private final org.springframework.cache.CacheManager cacheManager;

    @Bean
    public ApplicationRunner initData() {
        return args -> {
            log.info("Running idempotent database seeding check...");

            // 1. Roles
            Role adminRole = roleRepo.findByName("ROLE_ADMIN").orElseGet(() ->
                    roleRepo.save(Role.builder().name("ROLE_ADMIN").description("System Administrator").build()));
            Role managerRole = roleRepo.findByName("ROLE_MANAGER").orElseGet(() ->
                    roleRepo.save(Role.builder().name("ROLE_MANAGER").description("Manager").build()));
            Role whRole = roleRepo.findByName("ROLE_WAREHOUSE_MANAGER").orElseGet(() ->
                    roleRepo.save(Role.builder().name("ROLE_WAREHOUSE_MANAGER").description("Warehouse Manager").build()));
            Role tmRole = roleRepo.findByName("ROLE_TRANSPORT_MANAGER").orElseGet(() ->
                    roleRepo.save(Role.builder().name("ROLE_TRANSPORT_MANAGER").description("Transport Manager").build()));
            Role empRole = roleRepo.findByName("ROLE_EMPLOYEE").orElseGet(() ->
                    roleRepo.save(Role.builder().name("ROLE_EMPLOYEE").description("Employee").build()));

            // 2. Users
            if (!userRepo.findByUsername("admin").isPresent()) {
                userRepo.save(User.builder().username("admin").email("admin@smartlogistics.com")
                        .password(passwordEncoder.encode("admin123")).firstName("System").lastName("Admin")
                        .department("IT").enabled(true).accountNonLocked(true).roles(Set.of(adminRole, managerRole)).build());
            }
            if (!userRepo.findByUsername("manager").isPresent()) {
                userRepo.save(User.builder().username("manager").email("manager@smartlogistics.com")
                        .password(passwordEncoder.encode("manager123")).firstName("John").lastName("Manager")
                        .department("Operations").enabled(true).accountNonLocked(true).roles(Set.of(managerRole)).build());
            }
            if (!userRepo.findByUsername("employee").isPresent()) {
                userRepo.save(User.builder().username("employee").email("employee@smartlogistics.com")
                        .password(passwordEncoder.encode("emp123")).firstName("Jane").lastName("Employee")
                        .department("Warehouse").enabled(true).accountNonLocked(true).roles(Set.of(empRole)).build());
            }

            // 3. Categories
            Category electronics = categoryRepo.findByCode("ELEC").orElseGet(() ->
                    categoryRepo.save(Category.builder().name("Electronics").code("ELEC").description("Electronic components").active(true).build()));
            Category machinery = categoryRepo.findByCode("MACH").orElseGet(() ->
                    categoryRepo.save(Category.builder().name("Machinery").code("MACH").description("Industrial machinery").active(true).build()));
            Category consumables = categoryRepo.findByCode("CONS").orElseGet(() ->
                    categoryRepo.save(Category.builder().name("Consumables").code("CONS").description("Consumable goods").active(true).build()));
            Category rawMaterials = categoryRepo.findByCode("RAW").orElseGet(() ->
                    categoryRepo.save(Category.builder().name("Raw Materials").code("RAW").description("Raw production materials").active(true).build()));
            Category packaging = categoryRepo.findByCode("PACK").orElseGet(() ->
                    categoryRepo.save(Category.builder().name("Packaging").code("PACK").description("Warehouse shipping packaging").active(true).build()));

            // 4. Suppliers
            Supplier techCorp = supplierRepo.findByCode("TC001").orElseGet(() ->
                    supplierRepo.save(Supplier.builder().name("TechCorp Solutions").code("TC001").email("info@techcorp.com")
                            .phone("+1-555-0100").address("123 Tech Street").city("San Francisco").country("USA")
                            .contactPerson("Alice Johnson").status(Supplier.Status.ACTIVE).build()));
            Supplier globalParts = supplierRepo.findByCode("GP002").orElseGet(() ->
                    supplierRepo.save(Supplier.builder().name("Global Parts Inc").code("GP002").email("contact@globalparts.com")
                            .phone("+1-555-0200").address("456 Industrial Ave").city("Chicago").country("USA")
                            .contactPerson("Bob Smith").status(Supplier.Status.ACTIVE).build()));
            Supplier asiaTrade = supplierRepo.findByCode("AT003").orElseGet(() ->
                    supplierRepo.save(Supplier.builder().name("Asia Trade Hub").code("AT003").email("trade@asiatrade.com")
                            .phone("+86-10-5555-0300").address("789 Commerce Rd").city("Shanghai").country("China")
                            .contactPerson("Chen Wei").status(Supplier.Status.ACTIVE).build()));
            Supplier apexSupplies = supplierRepo.findByCode("ALS04").orElseGet(() ->
                    supplierRepo.save(Supplier.builder().name("Apex Logistical Supplies").code("ALS04").email("sales@apexlog.com")
                            .phone("+44-20-7946-0958").address("10 Logistics Park").city("London").country("UK")
                            .contactPerson("Emma Watson").status(Supplier.Status.ACTIVE).build()));

            // 5. Products
            Product p1 = productRepo.findBySku("ICU-001").orElseGet(() ->
                    productRepo.save(Product.builder().name("Industrial Controller Unit").sku("ICU-001").description("Advanced PLC controller for industrial automation").unitPrice(new BigDecimal("1299.99")).costPrice(new BigDecimal("850.00")).unit("PCS").brand("Siemens").reorderPoint(5).reorderQuantity(20).status(Product.Status.ACTIVE).category(electronics).supplier(techCorp).build()));
            Product p2 = productRepo.findBySku("HPA-002").orElseGet(() ->
                    productRepo.save(Product.builder().name("Hydraulic Pump Assembly").sku("HPA-002").description("Heavy-duty hydraulic pump for industrial use").unitPrice(new BigDecimal("2499.00")).costPrice(new BigDecimal("1600.00")).unit("PCS").brand("Bosch").reorderPoint(3).reorderQuantity(10).status(Product.Status.ACTIVE).category(machinery).supplier(globalParts).build()));
            Product p3 = productRepo.findBySku("SSB-003").orElseGet(() ->
                    productRepo.save(Product.builder().name("Stainless Steel Bolts M12").sku("SSB-003").description("M12 stainless steel hex bolts, box of 100").unitPrice(new BigDecimal("45.99")).costPrice(new BigDecimal("22.00")).unit("BOX").brand("FastenerPro").reorderPoint(50).reorderQuantity(200).status(Product.Status.ACTIVE).category(consumables).supplier(asiaTrade).build()));
            Product p4 = productRepo.findBySku("CW-004").orElseGet(() ->
                    productRepo.save(Product.builder().name("Copper Wire 2.5mm").sku("CW-004").description("2.5mm copper wire, 100m roll").unitPrice(new BigDecimal("189.99")).costPrice(new BigDecimal("110.00")).unit("ROLL").brand("CopperTech").reorderPoint(20).reorderQuantity(100).status(Product.Status.ACTIVE).category(rawMaterials).supplier(globalParts).build()));
            Product p5 = productRepo.findBySku("SH-005").orElseGet(() ->
                    productRepo.save(Product.builder().name("Safety Helmet Type-E").sku("SH-005").description("Industrial safety helmet, CE certified").unitPrice(new BigDecimal("34.99")).costPrice(new BigDecimal("15.00")).unit("PCS").brand("SafeGuard").reorderPoint(30).reorderQuantity(100).status(Product.Status.ACTIVE).category(consumables).supplier(techCorp).build()));
            Product p6 = productRepo.findBySku("EM-006").orElseGet(() ->
                    productRepo.save(Product.builder().name("Electric Motor 5HP").sku("EM-006").description("Three-phase induction electric motor 5HP").unitPrice(new BigDecimal("780.00")).costPrice(new BigDecimal("520.00")).unit("PCS").brand("ABB").reorderPoint(4).reorderQuantity(10).status(Product.Status.ACTIVE).category(machinery).supplier(globalParts).build()));
            Product p7 = productRepo.findBySku("BWR-007").orElseGet(() ->
                    productRepo.save(Product.builder().name("Bubble Wrap Roll").sku("BWR-007").description("Heavy-duty bubble wrap, 50m roll").unitPrice(new BigDecimal("25.00")).costPrice(new BigDecimal("11.50")).unit("ROLL").brand("WrapMaster").reorderPoint(10).reorderQuantity(40).status(Product.Status.ACTIVE).category(packaging).supplier(apexSupplies).build()));
            Product p8 = productRepo.findBySku("SFR-008").orElseGet(() ->
                    productRepo.save(Product.builder().name("Stretch Film Roll").sku("SFR-008").description("Pallet stretch wrap film, 500mm width").unitPrice(new BigDecimal("18.50")).costPrice(new BigDecimal("8.00")).unit("ROLL").brand("WrapMaster").reorderPoint(15).reorderQuantity(50).status(Product.Status.ACTIVE).category(packaging).supplier(apexSupplies).build()));
            Product p9 = productRepo.findBySku("PLC-009").orElseGet(() ->
                    productRepo.save(Product.builder().name("PLC Module 16-Point").sku("PLC-009").description("Siemens Simatic digital output expansion module").unitPrice(new BigDecimal("495.00")).costPrice(new BigDecimal("320.00")).unit("PCS").brand("Siemens").reorderPoint(5).reorderQuantity(15).status(Product.Status.ACTIVE).category(electronics).supplier(techCorp).build()));
            Product p10 = productRepo.findBySku("PC-010").orElseGet(() ->
                    productRepo.save(Product.builder().name("Pneumatic Cylinder D40").sku("PC-010").description("Double acting pneumatic air cylinder").unitPrice(new BigDecimal("145.00")).costPrice(new BigDecimal("90.00")).unit("PCS").brand("Festo").reorderPoint(8).reorderQuantity(30).status(Product.Status.ACTIVE).category(machinery).supplier(globalParts).build()));
            Product p11 = productRepo.findBySku("WG-011").orElseGet(() ->
                    productRepo.save(Product.builder().name("Work Gloves (Pair)").sku("WG-011").description("Heavy duty leather working gloves").unitPrice(new BigDecimal("12.99")).costPrice(new BigDecimal("5.50")).unit("PCS").brand("SafetyFirst").reorderPoint(100).reorderQuantity(500).status(Product.Status.ACTIVE).category(consumables).supplier(apexSupplies).build()));
            Product p12 = productRepo.findBySku("AR-012").orElseGet(() ->
                    productRepo.save(Product.builder().name("Aluminum Rod 10mm").sku("AR-012").description("10mm grade 6061 aluminum alloy rod, 3m").unitPrice(new BigDecimal("32.50")).costPrice(new BigDecimal("18.00")).unit("PCS").brand("Alcoa").reorderPoint(40).reorderQuantity(120).status(Product.Status.ACTIVE).category(rawMaterials).supplier(asiaTrade).build()));

            // 6. Warehouses
            Warehouse wh1 = warehouseRepo.findByCode("WH-001").orElseGet(() ->
                    warehouseRepo.save(Warehouse.builder().name("Main Distribution Center").code("WH-001").address("1000 Logistics Blvd").city("Los Angeles").state("CA").country("USA").zipCode("90001").managerName("Robert Chen").phone("+1-555-1001").totalCapacity(new BigDecimal("50000.0")).usedCapacity(new BigDecimal("32000.0")).capacityUnit("m²").status(Warehouse.Status.ACTIVE).build()));
            Warehouse wh2 = warehouseRepo.findByCode("WH-002").orElseGet(() ->
                    warehouseRepo.save(Warehouse.builder().name("East Coast Hub").code("WH-002").address("2000 Harbor Drive").city("New York").state("NY").country("USA").zipCode("10001").managerName("Sarah Miller").phone("+1-555-1002").totalCapacity(new BigDecimal("35000.0")).usedCapacity(new BigDecimal("18000.0")).capacityUnit("m²").status(Warehouse.Status.ACTIVE).build()));
            Warehouse wh3 = warehouseRepo.findByCode("WH-003").orElseGet(() ->
                    warehouseRepo.save(Warehouse.builder().name("Midwest Storage Facility").code("WH-003").address("3000 Central Ave").city("Chicago").state("IL").country("USA").zipCode("60601").managerName("Mike Davis").phone("+1-555-1003").totalCapacity(new BigDecimal("28000.0")).usedCapacity(new BigDecimal("9800.0")).capacityUnit("m²").status(Warehouse.Status.ACTIVE).build()));
            Warehouse wh4 = warehouseRepo.findByCode("WH-004").orElseGet(() ->
                    warehouseRepo.save(Warehouse.builder().name("Southern Logistics Center").code("WH-004").address("5000 Port Road").city("Houston").state("TX").country("USA").zipCode("77001").managerName("James Carter").phone("+1-555-1004").totalCapacity(new BigDecimal("40000.0")).usedCapacity(new BigDecimal("15000.0")).capacityUnit("m²").status(Warehouse.Status.ACTIVE).build()));
            Warehouse wh5 = warehouseRepo.findByCode("WH-005").orElseGet(() ->
                    warehouseRepo.save(Warehouse.builder().name("Northwest Transit Depot").code("WH-005").address("1200 Terminal Way").city("Seattle").state("WA").country("USA").zipCode("98101").managerName("Alex Mercer").phone("+1-555-1005").totalCapacity(new BigDecimal("15000.0")).usedCapacity(new BigDecimal("4500.0")).capacityUnit("m²").status(Warehouse.Status.ACTIVE).build()));
            Warehouse wh6 = warehouseRepo.findByCode("WH-006").orElseGet(() ->
                    warehouseRepo.save(Warehouse.builder().name("Rocky Mountain Storage").code("WH-006").address("880 Mountain View Rd").city("Denver").state("CO").country("USA").zipCode("80201").managerName("Diana Prince").phone("+1-555-1006").totalCapacity(new BigDecimal("20000.0")).usedCapacity(new BigDecimal("6000.0")).capacityUnit("m²").status(Warehouse.Status.ACTIVE).build()));

            // 7. Inventory
            if (inventoryRepo.count() == 0) {
                log.info("Seeding Inventory...");
                inventoryRepo.save(Inventory.builder().product(p1).warehouse(wh1).quantityOnHand(120).quantityReserved(15).binLocation("A1-01").averageCost(new BigDecimal("850.00")).build());
                inventoryRepo.save(Inventory.builder().product(p2).warehouse(wh1).quantityOnHand(45).quantityReserved(5).binLocation("B2-03").averageCost(new BigDecimal("1600.00")).build());
                inventoryRepo.save(Inventory.builder().product(p3).warehouse(wh2).quantityOnHand(80).quantityReserved(0).binLocation("C3-05").averageCost(new BigDecimal("22.00")).build());
                inventoryRepo.save(Inventory.builder().product(p4).warehouse(wh2).quantityOnHand(350).quantityReserved(50).binLocation("D4-02").averageCost(new BigDecimal("110.00")).build());
                inventoryRepo.save(Inventory.builder().product(p5).warehouse(wh3).quantityOnHand(250).quantityReserved(10).binLocation("E5-07").averageCost(new BigDecimal("15.00")).build());
                inventoryRepo.save(Inventory.builder().product(p6).warehouse(wh3).quantityOnHand(15).quantityReserved(2).binLocation("F6-02").averageCost(new BigDecimal("520.00")).build());
                inventoryRepo.save(Inventory.builder().product(p7).warehouse(wh4).quantityOnHand(100).quantityReserved(0).binLocation("G7-01").averageCost(new BigDecimal("11.50")).build());
                inventoryRepo.save(Inventory.builder().product(p8).warehouse(wh4).quantityOnHand(80).quantityReserved(5).binLocation("H8-04").averageCost(new BigDecimal("8.00")).build());
                inventoryRepo.save(Inventory.builder().product(p9).warehouse(wh1).quantityOnHand(30).quantityReserved(4).binLocation("A2-02").averageCost(new BigDecimal("320.00")).build());
                inventoryRepo.save(Inventory.builder().product(p10).warehouse(wh5).quantityOnHand(40).quantityReserved(0).binLocation("S1-01").averageCost(new BigDecimal("90.00")).build());
                inventoryRepo.save(Inventory.builder().product(p11).warehouse(wh3).quantityOnHand(600).quantityReserved(100).binLocation("E2-01").averageCost(new BigDecimal("5.50")).build());
                inventoryRepo.save(Inventory.builder().product(p12).warehouse(wh6).quantityOnHand(95).quantityReserved(10).binLocation("R1-05").averageCost(new BigDecimal("18.00")).build());
            }

            // 8. Customers
            Customer c1 = customerRepo.findByCode("CUS-001").orElseGet(() ->
                    customerRepo.save(Customer.builder().name("Acme Manufacturing Corp").code("CUS-001").email("procurement@acme.com").phone("+1-555-2001").billingAddress("100 Factory Lane, Detroit, MI 48201").shippingAddress("100 Factory Lane, Detroit, MI 48201").city("Detroit").state("MI").country("USA").contactPerson("Tom Wilson").customerType(Customer.CustomerType.CORPORATE).creditLimit(new BigDecimal("500000.00")).status(Customer.Status.ACTIVE).build()));
            Customer c2 = customerRepo.findByCode("CUS-002").orElseGet(() ->
                    customerRepo.save(Customer.builder().name("BuildRight Construction").code("CUS-002").email("supply@buildright.com").phone("+1-555-2002").billingAddress("200 Builder Ave, Houston, TX 77001").shippingAddress("200 Builder Ave, Houston, TX 77001").city("Houston").state("TX").country("USA").contactPerson("Lisa Brown").customerType(Customer.CustomerType.WHOLESALE).creditLimit(new BigDecimal("250000.00")).status(Customer.Status.ACTIVE).build()));
            Customer c3 = customerRepo.findByCode("CUS-003").orElseGet(() ->
                    customerRepo.save(Customer.builder().name("TechStart Systems").code("CUS-003").email("ops@techstart.io").phone("+1-555-2003").billingAddress("300 Innovation Park, Austin, TX 78701").shippingAddress("300 Innovation Park, Austin, TX 78701").city("Austin").state("TX").country("USA").contactPerson("James Lee").customerType(Customer.CustomerType.CORPORATE).creditLimit(new BigDecimal("150000.00")).status(Customer.Status.ACTIVE).build()));
            Customer c4 = customerRepo.findByCode("CUS-004").orElseGet(() ->
                    customerRepo.save(Customer.builder().name("Prime Distribution Ltd").code("CUS-004").email("orders@primedist.co.uk").phone("+44-161-496-0123").billingAddress("50 Logistics Way").shippingAddress("50 Logistics Way").city("Manchester").state("GMC").country("UK").contactPerson("Sarah Jenkins").customerType(Customer.CustomerType.RETAIL).creditLimit(new BigDecimal("300000.00")).status(Customer.Status.ACTIVE).build()));
            Customer c5 = customerRepo.findByCode("CUS-005").orElseGet(() ->
                    customerRepo.save(Customer.builder().name("Western Retailers Group").code("CUS-005").email("purchasing@westernretail.com").phone("+1-555-2005").billingAddress("500 Pine St, Seattle, WA 98101").shippingAddress("500 Pine St, Seattle, WA 98101").city("Seattle").state("WA").country("USA").contactPerson("Peter Parker").customerType(Customer.CustomerType.WHOLESALE).creditLimit(new BigDecimal("400000.00")).status(Customer.Status.ACTIVE).build()));
            Customer c6 = customerRepo.findByCode("CUS-006").orElseGet(() ->
                    customerRepo.save(Customer.builder().name("Midwest Distribution Partners").code("CUS-006").email("ops@midwestdist.com").phone("+1-555-2006").billingAddress("750 Nicollet Mall, Minneapolis, MN 55402").shippingAddress("750 Nicollet Mall, Minneapolis, MN 55402").city("Minneapolis").state("MN").country("USA").contactPerson("Bruce Banner").customerType(Customer.CustomerType.CORPORATE).creditLimit(new BigDecimal("600000.00")).status(Customer.Status.ACTIVE).build()));

            // 9. Routes
            Route r1 = routeRepo.findByCode("RT-001").orElseGet(() ->
                    routeRepo.save(Route.builder().name("LA to NYC Express").code("RT-001").origin("Los Angeles, CA").destination("New York, NY").distanceKm(new BigDecimal("4484")).estimatedHours(new BigDecimal("48.0")).status(Route.Status.ACTIVE).build()));
            Route r2 = routeRepo.findByCode("RT-002").orElseGet(() ->
                    routeRepo.save(Route.builder().name("LA to Chicago Midwest").code("RT-002").origin("Los Angeles, CA").destination("Chicago, IL").distanceKm(new BigDecimal("3245")).estimatedHours(new BigDecimal("36.0")).status(Route.Status.ACTIVE).build()));
            Route r3 = routeRepo.findByCode("RT-003").orElseGet(() ->
                    routeRepo.save(Route.builder().name("NYC to Chicago").code("RT-003").origin("New York, NY").destination("Chicago, IL").distanceKm(new BigDecimal("1272")).estimatedHours(new BigDecimal("15.0")).status(Route.Status.ACTIVE).build()));
            Route r4 = routeRepo.findByCode("RT-004").orElseGet(() ->
                    routeRepo.save(Route.builder().name("Houston to NY Depot").code("RT-004").origin("Houston, TX").destination("New York, NY").distanceKm(new BigDecimal("2620")).estimatedHours(new BigDecimal("30.0")).status(Route.Status.ACTIVE).build()));
            Route r5 = routeRepo.findByCode("RT-005").orElseGet(() ->
                    routeRepo.save(Route.builder().name("Seattle to LA Coastline").code("RT-005").origin("Seattle, WA").destination("Los Angeles, CA").distanceKm(new BigDecimal("1820")).estimatedHours(new BigDecimal("22.0")).status(Route.Status.ACTIVE).build()));
            Route r6 = routeRepo.findByCode("RT-006").orElseGet(() ->
                    routeRepo.save(Route.builder().name("Denver to Houston Pass").code("RT-006").origin("Denver, CO").destination("Houston, TX").distanceKm(new BigDecimal("1650")).estimatedHours(new BigDecimal("18.0")).status(Route.Status.ACTIVE).build()));

            // 10. Vehicles
            Vehicle v1 = vehicleRepo.findByLicensePlate("CA-TRK-001").orElseGet(() ->
                    vehicleRepo.save(Vehicle.builder().licensePlate("CA-TRK-001").make("Freightliner").model("Cascadia").year(2022).type(Vehicle.VehicleType.TRUCK).maxLoadCapacity(new BigDecimal("22000")).capacityUnit("kg").driverName("Carlos Rodriguez").driverPhone("+1-555-3001").status(Vehicle.VehicleStatus.AVAILABLE).insuranceExpiry(LocalDate.now().plusYears(1)).nextMaintenanceDate(LocalDate.now().plusMonths(3)).build()));
            Vehicle v2 = vehicleRepo.findByLicensePlate("NY-VAN-002").orElseGet(() ->
                    vehicleRepo.save(Vehicle.builder().licensePlate("NY-VAN-002").make("Mercedes-Benz").model("Sprinter").year(2021).type(Vehicle.VehicleType.VAN).maxLoadCapacity(new BigDecimal("3500")).capacityUnit("kg").driverName("David Park").driverPhone("+1-555-3002").status(Vehicle.VehicleStatus.ON_ROUTE).insuranceExpiry(LocalDate.now().plusYears(1)).nextMaintenanceDate(LocalDate.now().plusMonths(2)).build()));
            Vehicle v3 = vehicleRepo.findByLicensePlate("IL-TRK-003").orElseGet(() ->
                    vehicleRepo.save(Vehicle.builder().licensePlate("IL-TRK-003").make("Volvo").model("VNL 760").year(2023).type(Vehicle.VehicleType.TRUCK).maxLoadCapacity(new BigDecimal("18000")).capacityUnit("kg").driverName("Maria Santos").driverPhone("+1-555-3003").status(Vehicle.VehicleStatus.AVAILABLE).insuranceExpiry(LocalDate.now().plusYears(2)).nextMaintenanceDate(LocalDate.now().plusMonths(6)).build()));
            Vehicle v4 = vehicleRepo.findByLicensePlate("TX-TRK-004").orElseGet(() ->
                    vehicleRepo.save(Vehicle.builder().licensePlate("TX-TRK-004").make("Kenworth").model("T680").year(2024).type(Vehicle.VehicleType.TRUCK).maxLoadCapacity(new BigDecimal("24000")).capacityUnit("kg").driverName("James Miller").driverPhone("+1-555-3004").status(Vehicle.VehicleStatus.AVAILABLE).insuranceExpiry(LocalDate.now().plusYears(2)).nextMaintenanceDate(LocalDate.now().plusMonths(5)).build()));
            Vehicle v5 = vehicleRepo.findByLicensePlate("WA-VAN-005").orElseGet(() ->
                    vehicleRepo.save(Vehicle.builder().licensePlate("WA-VAN-005").make("Ford").model("Transit-350").year(2023).type(Vehicle.VehicleType.VAN).maxLoadCapacity(new BigDecimal("4000")).capacityUnit("kg").driverName("Peter Parker").driverPhone("+1-555-3005").status(Vehicle.VehicleStatus.AVAILABLE).insuranceExpiry(LocalDate.now().plusYears(1)).nextMaintenanceDate(LocalDate.now().plusMonths(4)).build()));
            Vehicle v6 = vehicleRepo.findByLicensePlate("CO-TRK-006").orElseGet(() ->
                    vehicleRepo.save(Vehicle.builder().licensePlate("CO-TRK-006").make("Peterbilt").model("579").year(2024).type(Vehicle.VehicleType.TRUCK).maxLoadCapacity(new BigDecimal("23000")).capacityUnit("kg").driverName("Bruce Banner").driverPhone("+1-555-3006").status(Vehicle.VehicleStatus.AVAILABLE).insuranceExpiry(LocalDate.now().plusYears(2)).nextMaintenanceDate(LocalDate.now().plusMonths(8)).build()));

            // 11. Orders
            CustomerOrder order1 = orderRepo.findByOrderNumber("ORD-20241201-0001").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0001").customer(c1).warehouse(wh1).status(CustomerOrder.OrderStatus.PROCESSING).paymentStatus(CustomerOrder.PaymentStatus.PAID).subtotal(new BigDecimal("25999.90")).taxAmount(new BigDecimal("2600.00")).shippingCost(new BigDecimal("500.00")).discountAmount(new BigDecimal("1000.00")).totalAmount(new BigDecimal("28099.90")).shippingAddress("100 Factory Lane, Detroit, MI 48201").shippingMethod("Express Freight").expectedDeliveryDate(LocalDate.now().plusDays(5)).createdBy("admin").build()));
            CustomerOrder order2 = orderRepo.findByOrderNumber("ORD-20241201-0002").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0002").customer(c2).warehouse(wh2).status(CustomerOrder.OrderStatus.PENDING).paymentStatus(CustomerOrder.PaymentStatus.PENDING).subtotal(new BigDecimal("12450.00")).taxAmount(new BigDecimal("1245.00")).shippingCost(new BigDecimal("350.00")).discountAmount(BigDecimal.ZERO).totalAmount(new BigDecimal("14045.00")).shippingAddress("200 Builder Ave, Houston, TX 77001").shippingMethod("Standard Freight").expectedDeliveryDate(LocalDate.now().plusDays(10)).createdBy("manager").build()));
            CustomerOrder order3 = orderRepo.findByOrderNumber("ORD-20241201-0003").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0003").customer(c3).warehouse(wh1).status(CustomerOrder.OrderStatus.SHIPPED).paymentStatus(CustomerOrder.PaymentStatus.PAID).subtotal(new BigDecimal("8750.00")).taxAmount(new BigDecimal("875.00")).shippingCost(new BigDecimal("200.00")).discountAmount(new BigDecimal("500.00")).totalAmount(new BigDecimal("9325.00")).shippingAddress("300 Innovation Park, Austin, TX 78701").shippingMethod("Standard Ground").expectedDeliveryDate(LocalDate.now().plusDays(3)).createdBy("admin").build()));
            CustomerOrder order4 = orderRepo.findByOrderNumber("ORD-20241201-0004").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0004").customer(c4).warehouse(wh3).status(CustomerOrder.OrderStatus.DELIVERED).paymentStatus(CustomerOrder.PaymentStatus.PAID).subtotal(new BigDecimal("450.00")).taxAmount(new BigDecimal("45.00")).shippingCost(new BigDecimal("50.00")).discountAmount(BigDecimal.ZERO).totalAmount(new BigDecimal("545.00")).shippingAddress("50 Logistics Way, Manchester").shippingMethod("Air Freight").expectedDeliveryDate(LocalDate.now().minusDays(2)).createdBy("manager").build()));
            CustomerOrder order5 = orderRepo.findByOrderNumber("ORD-20241201-0005").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0005").customer(c1).warehouse(wh4).status(CustomerOrder.OrderStatus.CANCELLED).paymentStatus(CustomerOrder.PaymentStatus.REFUNDED).subtotal(new BigDecimal("3200.00")).taxAmount(new BigDecimal("320.00")).shippingCost(new BigDecimal("150.00")).discountAmount(BigDecimal.ZERO).totalAmount(new BigDecimal("3670.00")).shippingAddress("100 Factory Lane, Detroit, MI 48201").shippingMethod("Express").expectedDeliveryDate(LocalDate.now().plusDays(1)).createdBy("admin").build()));
            CustomerOrder order6 = orderRepo.findByOrderNumber("ORD-20241201-0006").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0006").customer(c5).warehouse(wh5).status(CustomerOrder.OrderStatus.PROCESSING).paymentStatus(CustomerOrder.PaymentStatus.PAID).subtotal(new BigDecimal("1200.00")).taxAmount(new BigDecimal("120.00")).shippingCost(new BigDecimal("80.00")).discountAmount(BigDecimal.ZERO).totalAmount(new BigDecimal("1400.00")).shippingAddress("500 Pine St, Seattle, WA").shippingMethod("Standard Ground").expectedDeliveryDate(LocalDate.now().plusDays(4)).createdBy("manager").build()));
            CustomerOrder order7 = orderRepo.findByOrderNumber("ORD-20241201-0007").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0007").customer(c6).warehouse(wh3).status(CustomerOrder.OrderStatus.SHIPPED).paymentStatus(CustomerOrder.PaymentStatus.PAID).subtotal(new BigDecimal("850.00")).taxAmount(new BigDecimal("85.00")).shippingCost(new BigDecimal("40.00")).discountAmount(BigDecimal.ZERO).totalAmount(new BigDecimal("975.00")).shippingAddress("750 Nicollet Mall, Minneapolis, MN").shippingMethod("Express").expectedDeliveryDate(LocalDate.now().plusDays(2)).createdBy("admin").build()));
            CustomerOrder order8 = orderRepo.findByOrderNumber("ORD-20241201-0008").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0008").customer(c1).warehouse(wh1).status(CustomerOrder.OrderStatus.DELIVERED).paymentStatus(CustomerOrder.PaymentStatus.PAID).subtotal(new BigDecimal("220.00")).taxAmount(new BigDecimal("22.00")).shippingCost(new BigDecimal("20.00")).discountAmount(BigDecimal.ZERO).totalAmount(new BigDecimal("262.00")).shippingAddress("100 Factory Lane, Detroit, MI").shippingMethod("Standard Ground").expectedDeliveryDate(LocalDate.now().minusDays(3)).createdBy("manager").build()));
            CustomerOrder order9 = orderRepo.findByOrderNumber("ORD-20241201-0009").orElseGet(() ->
                    orderRepo.save(CustomerOrder.builder().orderNumber("ORD-20241201-0009").customer(c2).warehouse(wh4).status(CustomerOrder.OrderStatus.PENDING).paymentStatus(CustomerOrder.PaymentStatus.PENDING).subtotal(new BigDecimal("3100.00")).taxAmount(new BigDecimal("310.00")).shippingCost(new BigDecimal("120.00")).discountAmount(BigDecimal.ZERO).totalAmount(new BigDecimal("3530.00")).shippingAddress("200 Builder Ave, Houston, TX").shippingMethod("Freight").expectedDeliveryDate(LocalDate.now().plusDays(8)).createdBy("admin").build()));

            // 12. Shipments
            if (!shipmentRepo.findByTrackingNumber("SHP-202412010001").isPresent()) {
                shipmentRepo.save(Shipment.builder().trackingNumber("SHP-202412010001").order(order1).vehicle(v1).route(r2).status(Shipment.ShipmentStatus.IN_TRANSIT).originAddress("1000 Logistics Blvd, Los Angeles, CA").destinationAddress("100 Factory Lane, Detroit, MI").scheduledDate(LocalDate.now().minusDays(1)).estimatedDelivery(LocalDate.now().plusDays(4)).weight(new BigDecimal("850.0")).shippingCost(new BigDecimal("500.00")).driverName("Carlos Rodriguez").driverPhone("+1-555-3001").build());
            }
            if (!shipmentRepo.findByTrackingNumber("SHP-202412010002").isPresent()) {
                shipmentRepo.save(Shipment.builder().trackingNumber("SHP-202412010002").order(order3).vehicle(v2).route(r1).status(Shipment.ShipmentStatus.OUT_FOR_DELIVERY).originAddress("2000 Harbor Drive, New York, NY").destinationAddress("300 Innovation Park, Austin, TX").scheduledDate(LocalDate.now().minusDays(3)).estimatedDelivery(LocalDate.now().plusDays(1)).weight(new BigDecimal("320.5")).shippingCost(new BigDecimal("200.00")).driverName("David Park").driverPhone("+1-555-3002").build());
            }
            if (!shipmentRepo.findByTrackingNumber("SHP-202412010003").isPresent()) {
                shipmentRepo.save(Shipment.builder().trackingNumber("SHP-202412010003").order(order4).vehicle(v3).route(r3).status(Shipment.ShipmentStatus.DELIVERED).originAddress("3000 Central Ave, Chicago, IL").destinationAddress("50 Logistics Way, Manchester").scheduledDate(LocalDate.now().minusDays(5)).estimatedDelivery(LocalDate.now().minusDays(2)).actualDeliveryDate(LocalDate.now().minusDays(2)).weight(new BigDecimal("120.0")).shippingCost(new BigDecimal("50.00")).driverName("Maria Santos").driverPhone("+1-555-3003").build());
            }
            if (!shipmentRepo.findByTrackingNumber("SHP-202412010004").isPresent()) {
                shipmentRepo.save(Shipment.builder().trackingNumber("SHP-202412010004").order(order7).vehicle(v4).route(r3).status(Shipment.ShipmentStatus.IN_TRANSIT).originAddress("3000 Central Ave, Chicago, IL").destinationAddress("750 Nicollet Mall, Minneapolis, MN").scheduledDate(LocalDate.now().minusDays(1)).estimatedDelivery(LocalDate.now().plusDays(2)).weight(new BigDecimal("240.0")).shippingCost(new BigDecimal("40.00")).driverName("James Miller").driverPhone("+1-555-3004").build());
            }
            if (!shipmentRepo.findByTrackingNumber("SHP-202412010005").isPresent()) {
                shipmentRepo.save(Shipment.builder().trackingNumber("SHP-202412010005").order(order8).vehicle(v1).route(r2).status(Shipment.ShipmentStatus.DELIVERED).originAddress("1000 Logistics Blvd, Los Angeles, CA").destinationAddress("100 Factory Lane, Detroit, MI").scheduledDate(LocalDate.now().minusDays(4)).estimatedDelivery(LocalDate.now().minusDays(1)).actualDeliveryDate(LocalDate.now().minusDays(1)).weight(new BigDecimal("50.0")).shippingCost(new BigDecimal("20.00")).driverName("Carlos Rodriguez").driverPhone("+1-555-3001").build());
            }
            if (!shipmentRepo.findByTrackingNumber("SHP-202412010006").isPresent()) {
                shipmentRepo.save(Shipment.builder().trackingNumber("SHP-202412010006").order(order6).vehicle(v5).route(r5).status(Shipment.ShipmentStatus.FAILED).originAddress("1200 Terminal Way, Seattle, WA").destinationAddress("500 Pine St, Seattle, WA").scheduledDate(LocalDate.now().minusDays(1)).estimatedDelivery(LocalDate.now()).weight(new BigDecimal("350.0")).shippingCost(new BigDecimal("80.00")).driverName("Peter Parker").driverPhone("+1-555-3005").build());
            }

            // 13. Stock Movements
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120101").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120101").product(p1).fromWarehouse(null).toWarehouse(wh1).movementType(StockMovement.MovementType.INBOUND).quantity(150).unitCost(new BigDecimal("850.00")).totalCost(new BigDecimal("127500.00")).reason("Initial stock reception from supplier TechCorp Solutions").performedBy("admin").approvedBy("admin").createdAt(LocalDateTime.now().minusDays(5)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120102").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120102").product(p2).fromWarehouse(null).toWarehouse(wh1).movementType(StockMovement.MovementType.INBOUND).quantity(50).unitCost(new BigDecimal("1600.00")).totalCost(new BigDecimal("80000.00")).reason("Restocking of Hydraulic Pumps from Global Parts").performedBy("manager").approvedBy("admin").createdAt(LocalDateTime.now().minusDays(3)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120103").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120103").product(p1).fromWarehouse(wh1).toWarehouse(wh2).movementType(StockMovement.MovementType.TRANSFER).quantity(30).unitCost(new BigDecimal("850.00")).totalCost(new BigDecimal("25500.00")).reason("Stock transfer to East Coast Hub for upcoming orders").performedBy("employee").approvedBy("manager").createdAt(LocalDateTime.now().minusDays(2)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120104").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120104").product(p2).fromWarehouse(wh1).toWarehouse(null).movementType(StockMovement.MovementType.OUTBOUND).quantity(5).unitCost(new BigDecimal("1600.00")).totalCost(new BigDecimal("8000.00")).reason("Fulfillment for Order #ORD-20241201-0001").performedBy("employee").approvedBy("manager").createdAt(LocalDateTime.now().minusDays(1)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120105").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120105").product(p3).fromWarehouse(null).toWarehouse(wh3).movementType(StockMovement.MovementType.INBOUND).quantity(300).unitCost(new BigDecimal("15.00")).totalCost(new BigDecimal("4500.00")).reason("Purchase order PO-98013 reception").performedBy("employee").approvedBy("manager").createdAt(LocalDateTime.now().minusDays(4)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120106").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120106").product(p2).fromWarehouse(null).toWarehouse(wh3).movementType(StockMovement.MovementType.INBOUND).quantity(20).unitCost(new BigDecimal("520.00")).totalCost(new BigDecimal("10400.00")).reason("Reorder replenishment from Global Parts").performedBy("manager").approvedBy("admin").createdAt(LocalDateTime.now().minusDays(3)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120107").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120107").product(p3).fromWarehouse(null).toWarehouse(wh2).movementType(StockMovement.MovementType.INBOUND).quantity(100).unitCost(new BigDecimal("22.00")).totalCost(new BigDecimal("2200.00")).reason("Restocking of Bolts Box").performedBy("employee").approvedBy("manager").createdAt(LocalDateTime.now().minusDays(3)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120108").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120108").product(p1).fromWarehouse(null).toWarehouse(wh2).movementType(StockMovement.MovementType.INBOUND).quantity(400).unitCost(new BigDecimal("110.00")).totalCost(new BigDecimal("44000.00")).reason("Initial raw copper roll arrivals").performedBy("manager").approvedBy("admin").createdAt(LocalDateTime.now().minusDays(6)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120109").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120109").product(p9).fromWarehouse(null).toWarehouse(wh1).movementType(StockMovement.MovementType.INBOUND).quantity(80).unitCost(new BigDecimal("320.00")).totalCost(new BigDecimal("25600.00")).reason("Initial stocking of 16-point modules").performedBy("admin").approvedBy("admin").createdAt(LocalDateTime.now().minusDays(5)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120110").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120110").product(p2).fromWarehouse(wh1).toWarehouse(wh5).movementType(StockMovement.MovementType.TRANSFER).quantity(15).unitCost(new BigDecimal("1600.00")).totalCost(new BigDecimal("24000.00")).reason("Transfer of heavy hydraulic pumps to Seattle hub").performedBy("employee").approvedBy("manager").createdAt(LocalDateTime.now().minusDays(2)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120111").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120111").product(p3).fromWarehouse(wh3).toWarehouse(null).movementType(StockMovement.MovementType.OUTBOUND).quantity(50).unitCost(new BigDecimal("22.00")).totalCost(new BigDecimal("1100.00")).reason("Outbound fulfillment order RT-889").performedBy("employee").approvedBy("manager").createdAt(LocalDateTime.now().minusDays(1)).build());
            }
            if (!stockMovementRepo.findByReferenceNumber("MV-2024120112").isPresent()) {
                stockMovementRepo.save(StockMovement.builder().referenceNumber("MV-2024120112").product(p12).fromWarehouse(wh6).toWarehouse(null).movementType(StockMovement.MovementType.ADJUSTMENT).quantity(-2).unitCost(new BigDecimal("18.00")).totalCost(new BigDecimal("-36.00")).reason("Scrapped damaged rods due to warehouse transport leakage").performedBy("manager").approvedBy("admin").createdAt(LocalDateTime.now().minusDays(3)).build());
            }

            // Clear dashboard cache to refresh KPI statistics instantly
            try {
                org.springframework.cache.Cache cache = cacheManager.getCache("dashboardStats");
                if (cache != null) {
                    cache.clear();
                    log.info("Cleared dashboardStats cache to refresh KPI metrics.");
                }
            } catch (Exception e) {
                log.warn("Could not clear dashboard cache: {}", e.getMessage());
            }

            log.info("Database seeding verification completed successfully.");
        };
    }
}