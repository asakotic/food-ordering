export type Dish = {
    id: number;
    name: string;
    quantity: number;
}

export type Orderer = {
  id: number;
  status: string;
  createdBy: string;
  createdAt: string;
  dishes: Dish[];
}