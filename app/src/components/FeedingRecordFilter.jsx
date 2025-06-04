import React from "react";

export default function FeedingRecordFilter({
                                                  filterType,
                                                  filterValue,
                                                  onFilterChange
                                              }) {

    const handleTypeChange = (e) => {
        const type = e.target.value;
        onFilterChange(type, ""); // Reset filter value until chosen
    };

    const handleValueChange = (e) => {
        const value = e.target.value;
        onFilterChange(filterType, value);
    };

    return (
        <div className="flex flex-col md:flex-row gap-3 items-start md:items-center mb-4">
            <div>
                <label className="font-semibold mr-2">Filter By:</label>
                <select value={filterType} onChange={handleTypeChange} className="border rounded px-2 py-1">
                    <option value="all">All</option>
                    <option value="date">Date</option>
                </select>
            </div>

            {filterType !== "all" && (
                <div>
                    <label className="font-semibold mr-2">Value:</label>
                    <input
                        type="date"
                        className="ml-2 border px-2 py-1 rounded"
                        value={filterValue}
                        onChange={handleValueChange}
                    />
                </div>
            )}
        </div>
    );
}
