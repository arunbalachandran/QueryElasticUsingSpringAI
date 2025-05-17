import axios from 'axios';
import { type Order } from '../types/Order';

const API_BASE_URL = 'http://localhost:8080/api/v1';

export const api = {
    getAllOrders: async (): Promise<Order[]> => {
        const response = await axios.get(`${API_BASE_URL}/orders`);
        return response.data;
    },

    searchOrders: async (query: string): Promise<Order[]> => {
        const response = await axios.post(`${API_BASE_URL}/elastic/query`, {"query": query});
        return response.data;
    },

    createOrder: async (order: Omit<Order, 'id' | 'createdTime' | 'updatedTime'>): Promise<Order> => {
        const response = await axios.post(`${API_BASE_URL}/orders`, order);
        return response.data;
    }
}; 