package com.athmarine.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "service_category_equipment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ServiceCategoryEquipmentMapping {

    @Column
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name="vendorService_id", referencedColumnName = "id")
    private VendorServices vendorServices;

    @ManyToOne
    @JoinColumn(name="category_id", referencedColumnName = "id")
    private MSTEquipmentCategory mstEquipmentCategory;

    @ManyToOne
    @JoinColumn(name="equipment_id", referencedColumnName = "id")
    private Equipment equipment;

    @ManyToOne
    @JoinColumn(name="manufacture_id", referencedColumnName = "id")
    private MSTManufacturer mstManufacturer;

}
