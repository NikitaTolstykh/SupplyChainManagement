import React from "react";
import Table from "../../../components/ui/Table.tsx";
import Button from "../../../components/ui/Button.tsx";
import type {UserResponseDto} from "../../../lib/types/AdminDtos.ts";

interface WorkerTableProps {
    workers: UserResponseDto[];
    onEdit: (worker: UserResponseDto) => void;
    onDelete: (id: number) => void;

}const WorkerTable: React.FC<WorkerTableProps> = ({ workers, onEdit, onDelete }) => {
    const columns = [
        { header: 'ID', accessor: 'id' as keyof UserResponseDto },
        { header: 'Email', accessor: 'email' as keyof UserResponseDto },
        { header: 'First Name', accessor: 'firstName' as keyof UserResponseDto },
        { header: 'Last Name', accessor: 'lastName' as keyof UserResponseDto },
        { header: 'Phone', accessor: 'phone' as keyof UserResponseDto },
        { header: 'Role', accessor: 'role' as keyof UserResponseDto },
    ];

    return (
        <Table
            columns={columns}
            data={workers}
            actions={(worker) => (
                <div className="flex gap-2">
                    <Button
                        variant="secondary"
                        onClick={() => onEdit(worker)}
                        className="text-sm"
                    >
                        Edit
                    </Button>
                    <Button
                        variant="danger"
                        onClick={() => onDelete(worker.id)}
                        className="text-sm"
                    >
                        Delete
                    </Button>
                </div>
            )}
        />
    );
};

export default WorkerTable;