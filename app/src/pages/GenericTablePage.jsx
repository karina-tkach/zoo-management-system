import {useNavigate} from "react-router-dom";
import Pagination from "../components/Pagination.jsx";

export default function GenericTablePage({
                                             title,
                                             data,
                                             columns,
                                             getActions = (_row) => [],
                                             withPagination = true,
                                             page,
                                             setPage,
                                             totalPages,
                                             shouldScroll,
                                             setShouldScroll,
                                             addButtonPath,
                                             addButtonText = "Add",
                                             emptyMessage = "No records found",
                                             searchBarComponent = null,
                                         }) {
    const navigate = useNavigate();
    const hasActions = data.some(row => getActions(row).length > 0);

    return (
        <div className="w-full mx-auto px-4 sm:px-6 lg:px-8 py-6 scroll-target">
            <div className="flex justify-between items-center mb-6">
                <h2 className="text-2xl font-semibold text-gray-800">{title}</h2>
                {addButtonPath && (
                    <button
                        onClick={() => navigate(addButtonPath)}
                        className="bg-blue-600 hover:bg-blue-700 text-white px-4 py-2 rounded-md shadow"
                    >
                        {addButtonText}
                    </button>
                )}
            </div>

            {searchBarComponent && (
                <div className="mb-4">
                    {searchBarComponent}
                </div>
            )}

            <div className="overflow-x-auto border border-gray-200 rounded-md shadow-sm">
                <table className="min-w-[1000px] w-full divide-y divide-gray-200">
                    <thead className="bg-gray-50">
                    <tr className="divide-x divide-gray-200">
                        {columns.map((col, i) => (
                            <th
                                key={i}
                                className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
                            >
                                {col.name}
                            </th>
                        ))}
                        {hasActions && (
                            <th className="px-4 py-2 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">
                                Actions
                            </th>
                        )}
                    </tr>
                    </thead>
                    <tbody className="bg-white divide-y divide-gray-200">
                    {data.length === 0 ? (
                        <tr>
                            <td colSpan={columns.length + (getActions ? 1 : 0)} className="text-center py-4 text-gray-500 italic">
                                {emptyMessage}
                            </td>
                        </tr>
                    ) : (
                        data.map((row, rowIndex) => (
                            <tr key={row.id ?? rowIndex} className="hover:bg-gray-50 divide-x divide-gray-200">
                                {columns.map((col, colIndex) => (
                                    <td
                                        key={colIndex}
                                        className={`px-4 py-3 text-gray-700 ${col?.isWrappable ? `whitespace-normal ${col?.breakMode || "break-words"}` : "whitespace-nowrap"}`}
                                    >
                                        {col.value(row)}
                                    </td>
                                ))}

                                {hasActions && (
                                <td className="px-4 py-3 whitespace-nowrap space-x-2">
                                    {getActions(row).map((action, i) => (
                                        <span key={i}>{action}</span>
                                    ))}
                                </td>
                                )}
                            </tr>
                        ))
                    )}
                    </tbody>
                </table>
            </div>

            {withPagination && (
                <Pagination
                currentPage={page}
                totalPages={totalPages}
                setCurrentPage={setPage}
                shouldScroll={shouldScroll}
                setShouldScroll={setShouldScroll}
            />)}
        </div>
    );
}
