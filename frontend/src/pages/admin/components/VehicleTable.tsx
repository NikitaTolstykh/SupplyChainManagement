import React from 'react';
import Table from "../../../components/ui/Table.tsx";
import Button from "../../../components/ui/Button.tsx";
import type {VehicleDto} from "../../../lib/types/AdminDtos.ts";

interface VehicleTableProps {
    vehicles: VehicleDto[];
    onEdit: (vehicle: VehicleDto) => void;
    onDelete: (id: number) => void;
}

const VehicleTable: React.FC<VehicleTableProps> = ({ vehicles, onEdit, onDelete }) => {
    const columns = [
        { header: 'ID', accessor: 'id' as keyof VehicleDto },
        { header: 'Brand', accessor: 'brand' as keyof VehicleDto },
        { header: 'Model', accessor: 'model' as keyof VehicleDto },
        { header: 'Color', accessor: 'color' as keyof VehicleDto },
        { header: 'License Plate', accessor: 'licensePlate' as keyof VehicleDto },
        { header: 'Driver ID', accessor: 'driverId' as keyof VehicleDto },
    ];

    return (
        <Table<VehicleDto>
            columns={columns}
            data={vehicles}
            actions={(vehicle) => (
                <div className="flex gap-2">
                    <Button
                        variant="secondary"
                        onClick={() => onEdit(vehicle)}
                        className="text-sm"
                    >
                        Edit
                    </Button>
                    <Button
                        variant="danger"
                        onClick={() => onDelete(vehicle.id!)}
                        className="text-sm"
                    >
                        Delete
                    </Button>
                </div>
            )}
        />

    );
};

export default VehicleTable;