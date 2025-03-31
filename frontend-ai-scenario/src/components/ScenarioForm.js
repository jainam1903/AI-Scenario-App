import React, { useState } from 'react';

const ScenarioForm = ({ onSubmit, isLoading }) => {
  const [scenario, setScenario] = useState('');
  const [constraints, setConstraints] = useState(['', '', '']);
  
  const handleAddConstraint = () => {
    setConstraints([...constraints, '']);
  };
  
  const handleRemoveConstraint = (index) => {
    const newConstraints = [...constraints];
    newConstraints.splice(index, 1);
    setConstraints(newConstraints);
  };
  
  const handleConstraintChange = (index, value) => {
    const newConstraints = [...constraints];
    newConstraints[index] = value;
    setConstraints(newConstraints);
  };
  
  const handleSubmit = (e) => {
    e.preventDefault();
    const filteredConstraints = constraints.filter(c => c.trim() !== '');
    onSubmit({ scenario, constraints: filteredConstraints });
  };
  
  return (
    <div className="bg-white p-6 rounded-lg shadow-md">
      <h2 className="text-2xl font-bold mb-4">Scenario Analysis</h2>
      <form onSubmit={handleSubmit}>
        <div className="mb-4">
          <label htmlFor="scenario" className="block text-gray-700 font-medium mb-2">
            Scenario Description
          </label>
          <textarea
            id="scenario"
            className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
            rows="5"
            placeholder="Describe your scenario in detail..."
            value={scenario}
            onChange={(e) => setScenario(e.target.value)}
            required
          />
        </div>
        
        <div className="mb-4">
          <label className="block text-gray-700 font-medium mb-2">
            Key Constraints
          </label>
          {constraints.map((constraint, index) => (
            <div key={index} className="flex mb-2">
              <input
                type="text"
                className="flex-grow px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder={`Constraint ${index + 1} (e.g., Budget: $10,000)`}
                value={constraint}
                onChange={(e) => handleConstraintChange(index, e.target.value)}
              />
              {index > 0 && (
                <button
                  type="button"
                  className="ml-2 px-3 py-2 bg-red-500 text-white rounded-md hover:bg-red-600"
                  onClick={() => handleRemoveConstraint(index)}
                >
                  Remove
                </button>
              )}
            </div>
          ))}
          <button
            type="button"
            className="mt-2 px-3 py-2 bg-gray-200 text-gray-700 rounded-md hover:bg-gray-300"
            onClick={handleAddConstraint}
          >
            + Add Another Constraint
          </button>
        </div>
        
        <button
          type="submit"
          className="w-full px-4 py-2 bg-blue-600 text-white font-medium rounded-md hover:bg-blue-700 focus:outline-none focus:ring-2 focus:ring-blue-500 disabled:bg-blue-300"
          disabled={isLoading}
        >
          {isLoading ? 'Analyzing...' : 'Analyze Scenario'}
        </button>
      </form>
    </div>
  );
};

export default ScenarioForm; 