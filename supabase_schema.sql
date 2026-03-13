-- Enable Row Level Security (RLS) on all tables is a best practice
-- We will enable it table by table.

-- 1. USERS TABLE
create table public.users (
  id uuid references auth.users not null primary key,
  email text,
  full_name text,
  avatar_url text,
  eco_score int default 0,
  -- Total lifetime coins earned (kept for analytics / history)
  total_gc int default 0,
  -- Current spendable coin balance for the user
  coins int default 0,
  created_at timestamptz default now()
);

alter table public.users enable row level security;

-- Policy: Users can read their own profile
create policy "Users can read own profile"
  on public.users for select
  using ( auth.uid() = id );

-- Policy: Users can update their own profile
create policy "Users can update own profile"
  on public.users for update
  using ( auth.uid() = id );

-- Trigger to handle new user signup automatically
create or replace function public.handle_new_user()
returns trigger as $$
begin
  insert into public.users (id, email, full_name, avatar_url)
  values (
    new.id,
    new.email,
    new.raw_user_meta_data->>'full_name',
    new.raw_user_meta_data->>'avatar_url'
  );
  return new;
end;
$$ language plpgsql security definer;

create trigger on_auth_user_created
  after insert on auth.users
  for each row execute procedure public.handle_new_user();


-- 2. CHALLENGES TABLE
create table public.challenges (
  id uuid default gen_random_uuid() primary key,
  title text not null,
  description text,
  cover_image_url text,
  reward_gc int default 0,
  start_date timestamptz,
  end_date timestamptz,
  is_active boolean default true,
  created_at timestamptz default now()
);

alter table public.challenges enable row level security;

-- Policy: Everyone can read active challenges
create policy "Public read access for challenges"
  on public.challenges for select
  using ( true );


-- 3. MISSIONS TABLE
create table public.missions (
  id uuid default gen_random_uuid() primary key,
  title text not null,
  description text,
  icon_type text, -- Maps to MissionIcon enum (TreePine, Recycle, Leaf, Users, Trash2, Zap)
  gc_reward int default 0,
  challenge_id uuid references public.challenges(id), -- Nullable, generic missions vs challenge specific
  created_at timestamptz default now()
);

alter table public.missions enable row level security;

-- Policy: Everyone can read missions
create policy "Public read access for missions"
  on public.missions for select
  using ( true );


-- 4. SUBMISSIONS TABLE
create table public.submissions (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) not null,
  mission_id uuid references public.missions(id) not null,
  challenge_id uuid references public.challenges(id),
  description text,
  image_url text,
  before_image_url text,
  after_image_url text,
  latitude float8,
  longitude float8,
  status text default 'pending', -- pending, approved, rejected
  rejected_reason text,
  created_at timestamptz default now()
);

alter table public.submissions enable row level security;

-- Policy: Users can view their own submissions
create policy "Users can view own submissions"
  on public.submissions for select
  using ( auth.uid() = user_id );

-- Policy: Users can create submissions
create policy "Users can create submissions"
  on public.submissions for insert
  with check ( auth.uid() = user_id );


-- 5. REWARDS TABLE (SHOP)
create table public.rewards (
  id uuid default gen_random_uuid() primary key,
  title text not null,
  description text,
  category text, -- Travel, Eco Store, Lifestyle, etc.
  gc_cost int default 0,
  image_url text,
  discount_label text, -- "20% OFF", etc.
  created_at timestamptz default now()
);

alter table public.rewards enable row level security;

-- Policy: Public read access for rewards
create policy "Public read access for rewards"
  on public.rewards for select
  using ( true );


-- 6. TRANSACTIONS TABLE
create table public.transactions (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) not null,
  amount int not null, -- Positive = Earned, Negative = Spent
  description text,
  type text, -- 'earn', 'redeem'
  related_submission_id uuid references public.submissions(id),
  related_reward_id uuid references public.rewards(id),
  created_at timestamptz default now()
);

alter table public.transactions enable row level security;

-- Policy: Users can read own transactions
create policy "Users can read own transactions"
  on public.transactions for select
  using ( auth.uid() = user_id );

-- Policy: Only system/functions can insert (usually), but for MVP let users insert via API (NOT SECURE for prod, but okay for MVP if RLS checks user_id)
-- Better approach: Use a Database Function to redeem rewards to ensure atomic balances.
-- For now, we allow insert if user owns the record.
create policy "Users can insert own transactions"
  on public.transactions for insert
  with check ( auth.uid() = user_id );


-- SEED DATA (from app/src/main/java/com/greencoins/app/data/Models.kt)

-- Missions
insert into public.missions (title, description, icon_type, gc_reward) values
('Green Canopy', 'Plant a native tree', 'TreePine', 250),
('Cycle Loop', 'Verify recycling batch', 'Recycle', 150),
('Plastic-Free', 'Cleanup plastic waste', 'Leaf', 100),
('Community Pulse', 'NGO volunteer work', 'Users', 300),
('Eco-Clearance', 'Garbage cleanup', 'Trash2', 200),
('Wildcard', 'Propose eco-action', 'Zap', 50);

-- Active Challenges
insert into public.challenges (title, description, reward_gc, end_date, cover_image_url) values
('City Cleanup Drive', 'Join the city-wide cleanup marathon.', 800, now() + interval '2 days', 'https://images.unsplash.com/photo-1757801720436-032c2e5b58c6?q=80&w=400'),
('Solar Transition', 'Switch to solar energy solutions.', 1500, now() + interval '12 days', 'https://images.unsplash.com/photo-1759266039803-1f81c04bd4c0?q=80&w=400');

-- Featured Challenges
insert into public.challenges (title, description, reward_gc, is_active, cover_image_url) values
('Green Roof Initiative', 'Promoting green rooftops.', 5000, true, 'https://images.unsplash.com/photo-1607194402064-d0742de6d17b?q=80&w=600'),
('Coral Reef Revival', 'Protect our oceans.', 0, true, 'https://images.unsplash.com/photo-1741704445331-83ed820f0214?q=80&w=600');

-- Rewards
insert into public.rewards (title, category, gc_cost, image_url, discount_label) values
('Metro Pass (1 Month)', 'Travel', 1200, 'https://images.unsplash.com/photo-1712591009476-5fe03c2ea938?q=80&w=400', '20% OFF'),
('Eco Bottle Pro', 'Eco Store', 800, 'https://images.unsplash.com/photo-1760863264228-fa0792a2d894?q=80&w=400', 'FREE'),
('Forest Donation', 'Direct Donate', 500, 'https://images.unsplash.com/photo-1647220576336-f2e94680f3b8?q=80&w=400', null),
('Zero Waste Kit', 'Lifestyle', 1500, 'https://images.unsplash.com/photo-1759868412016-8b7da190992a?q=80&w=400', '15% OFF'),
-- Extra items from ShopRepository.kt
('₹50 Metro Recharge', 'Travel', 250, null, null),
('Bus Pass Discount', 'Travel', 400, null, null),
('Reusable Bottle', 'Eco Store', 300, null, null),
('Cloth Tote Bag', 'Eco Store', 200, null, null),
('Café Voucher', 'Lifestyle', 350, null, null);

-- Storage (Instructions)
-- 1. Create a public bucket 'mission-proofs'
-- 2. Create a public bucket 'avatars'
