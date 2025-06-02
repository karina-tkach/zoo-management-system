import React from "react";
import {useAuth} from "../context/AuthContext.jsx";

const filterOptions = {
    group: ["INVERTEBRATES", "FISH", "AMPHIBIANS", "REPTILES", "BIRDS", "MAMMALS"],
    habitat: ["DESERT", "GRASSLAND", "AQUATIC", "FOREST", "WETLAND", "MOUNTAIN", "POLAR", "SAVANNA"],
    healthStatus: ["HEALTHY", "SICK", "NEEDS_CHECK_UP"]
};

export default function AnimalFilter({
                                         filterType,
                                         filterValue,
                                         onFilterChange
                                     }) {
    const { user } = useAuth();
    const isAdmin = user?.roles.includes("ADMIN");

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
                    <option value="group">Animal Group</option>
                    <option value="habitat">Habitat Type</option>
                    {isAdmin && <option value="healthStatus">Health Status</option>}
                </select>
            </div>

            {filterType !== "all" && (
                <div>
                    <label className="font-semibold mr-2">Value:</label>
                    <select value={filterValue} onChange={handleValueChange} className="border rounded px-2 py-1">
                        <option value="" disabled={true}>-- Select Value --</option>
                        {filterOptions[filterType]?.map((option) => (
                            <option key={option} value={option}>
                                {option}
                            </option>
                        ))}
                    </select>
                </div>
            )}
        </div>
    );
}
