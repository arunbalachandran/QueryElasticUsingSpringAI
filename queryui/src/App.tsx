import { useState, useEffect } from 'react';
import {
    Container,
    Box,
    TextField,
    Button,
    Table,
    TableBody,
    TableCell,
    TableContainer,
    TableHead,
    TableRow,
    Paper,
    Dialog,
    DialogTitle,
    DialogContent,
    DialogActions,
} from '@mui/material';
import { Add as AddIcon, Search as SearchIcon, Refresh as RefreshIcon } from '@mui/icons-material';
import { api } from './services/api';
import { type Order } from './types/Order';

function App() {
    const [orders, setOrders] = useState<Order[]>([]);
    const [searchQuery, setSearchQuery] = useState('');
    const [isSearchMode, setIsSearchMode] = useState(false);
    const [isAddModalOpen, setIsAddModalOpen] = useState(false);
    const [newOrder, setNewOrder] = useState({
        productName: '',
        productQty: 0,
        productPrice: 0,
        productDescription: ''
    });

    const fetchOrders = async () => {
        try {
            const data = await api.getAllOrders();
            setOrders(data);
        } catch (error) {
            console.error('Error fetching orders:', error);
        }
    };

    const handleSearch = async () => {
        if (!searchQuery.trim()) return;
        
        try {
            const data = await api.searchOrders(searchQuery);
            setOrders(data);
            setIsSearchMode(true);
        } catch (error) {
            console.error('Error searching orders:', error);
        }
    };

    const handleReset = () => {
        setSearchQuery('');
        setIsSearchMode(false);
        fetchOrders();
    };

    const handleAddOrder = async () => {
        try {
            await api.createOrder(newOrder);
            setIsAddModalOpen(false);
            setNewOrder({
                productName: '',
                productQty: 0,
                productPrice: 0,
                productDescription: ''
            });
            fetchOrders();
        } catch (error) {
            console.error('Error creating order:', error);
        }
    };

    useEffect(() => {
        fetchOrders();
    }, []);

  return (
        <Container maxWidth="lg" sx={{ mt: 4, marginTop: 'none' }}>
            <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 3, p: 2, bgcolor: '#f5f5f5', borderRadius: 2, boxShadow: 1 }}>
                <Box sx={{ display: 'flex', gap: 2, flex: 1, mr: 2 }}>
                    <TextField
                        fullWidth
                        label="Search Orders"
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
                        sx={{ bgcolor: 'white', boxShadow: 'none' }}
                    />
                    <Button
                        variant="contained"
                        startIcon={<SearchIcon />}
                        onClick={handleSearch}
                    >
                        Search
                    </Button>
                    {isSearchMode && (
                        <Button
                            variant="outlined"
                            startIcon={<RefreshIcon />}
                            onClick={handleReset}
                        >
                            Show All
                        </Button>
                    )}
                </Box>
                <Button
                    variant="contained"
                    color="primary"
                    startIcon={<AddIcon />}
                    onClick={() => setIsAddModalOpen(true)}
                >
                    Add Order
                </Button>
            </Box>

            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>Product Name</TableCell>
                            <TableCell>Quantity</TableCell>
                            <TableCell>Price</TableCell>
                            <TableCell>Description</TableCell>
                            <TableCell>Created</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {orders.map((order) => (
                            <TableRow key={order.id}>
                                <TableCell>{order.productName}</TableCell>
                                <TableCell>{order.productQty}</TableCell>
                                <TableCell>${order.productPrice.toFixed(2)}</TableCell>
                                <TableCell>{order.productDescription}</TableCell>
                                <TableCell>
                                    {new Date(order.createdTime).toLocaleString()}
                                </TableCell>
                            </TableRow>
                        ))}
                    </TableBody>
                </Table>
            </TableContainer>

            <Dialog open={isAddModalOpen} onClose={() => setIsAddModalOpen(false)}>
                <DialogTitle>Add New Order</DialogTitle>
                <DialogContent>
                    <Box sx={{ display: 'flex', flexDirection: 'column', gap: 2, pt: 2 }}>
                        <TextField
                            label="Product Name"
                            value={newOrder.productName}
                            onChange={(e) => setNewOrder({ ...newOrder, productName: e.target.value })}
                        />
                        <TextField
                            label="Quantity"
                            type="number"
                            value={newOrder.productQty}
                            onChange={(e) => setNewOrder({ ...newOrder, productQty: parseInt(e.target.value) })}
                        />
                        <TextField
                            label="Price"
                            type="number"
                            value={newOrder.productPrice}
                            onChange={(e) => setNewOrder({ ...newOrder, productPrice: parseFloat(e.target.value) })}
                        />
                        <TextField
                            label="Description"
                            multiline
                            rows={3}
                            value={newOrder.productDescription}
                            onChange={(e) => setNewOrder({ ...newOrder, productDescription: e.target.value })}
                        />
                    </Box>
                </DialogContent>
                <DialogActions>
                    <Button onClick={() => setIsAddModalOpen(false)}>Cancel</Button>
                    <Button onClick={handleAddOrder} variant="contained">
                        Add Order
                    </Button>
                </DialogActions>
            </Dialog>
        </Container>
    );
}

export default App;
