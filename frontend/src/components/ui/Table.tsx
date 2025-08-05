import React from 'react';

interface TableProps<T> {
    columns: { header: string; accessor: keyof T }[];
    data: T[];
    actions?: (item: T) => React.ReactNode;
}

function Table<T extends { id: number | string }>({ columns, data, actions }: TableProps<T>) {
    return (
        <table className="min-w-full border-collapse border border-gray-300">
            <thead>
            <tr>
                {columns.map((col) => (
                    <th
                        key={String(col.accessor)}
                        className="border border-gray-300 px-4 py-2 bg-gray-100 text-left"
                    >
                        {col.header}
                    </th>
                ))}
                {actions && <th className="border border-gray-300 px-4 py-2 bg-gray-100">Actions</th>}
            </tr>
            </thead>
            <tbody>
            {data.map((item) => (
                <tr key={item.id} className="hover:bg-gray-50">
                    {columns.map((col) => (
                        <td key={String(col.accessor)} className="border border-gray-300 px-4 py-2">
                            {String(item[col.accessor])}
                        </td>
                    ))}
                    {actions && <td className="border border-gray-300 px-4 py-2">{actions(item)}</td>}
                </tr>
            ))}
            </tbody>
        </table>
    );
}

export default Table;
