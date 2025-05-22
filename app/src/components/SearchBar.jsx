import React from "react";
import { Search } from "lucide-react";

const SearchBar = ({ searchQuery, setSearchQuery }) => {
    return (
        <div className="flex items-center border border-gray-300 rounded-lg px-3 py-2 md:w-[260px] w-full bg-white shadow-sm mt-5">
            <Search className="w-5 h-5 text-gray-400 mr-2" />
            <input
                type="text"
                placeholder="Search by uuid or full name"
                value={searchQuery}
                onChange={(e) => setSearchQuery(e.target.value)}
                className="outline-none w-full text-gray-600 placeholder-gray-400 bg-transparent"
            />
        </div>
    );
};

export default SearchBar;
