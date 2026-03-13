-- GreenCoins - Fully Database-Driven Schema
-- Enable Row Level Security (RLS) on all tables

-- 1. USERS TABLE (extended for dynamic profile data)
create table if not exists public.users (
  id uuid references auth.users not null primary key,
  email text,
  full_name text,
  avatar_url text,
  eco_score int default 0,
  total_gc int default 0,
  coins int default 0,
  trees_planted int default 0,
  plastic_recycled_kg int default 0,
  co2_saved_kg int default 0,
  level int default 1,
  global_rank int,
  created_at timestamptz default now(),
  updated_at timestamptz default now()
);

-- Add new columns if migrating from existing schema
alter table public.users add column if not exists trees_planted int default 0;
alter table public.users add column if not exists plastic_recycled_kg int default 0;
alter table public.users add column if not exists co2_saved_kg int default 0;
alter table public.users add column if not exists level int default 1;
alter table public.users add column if not exists global_rank int;
alter table public.users add column if not exists updated_at timestamptz default now();
alter table public.users add column if not exists phone text;
alter table public.users add column if not exists city text;

alter table public.users enable row level security;

create policy "Users can read own profile"
  on public.users for select using ( auth.uid() = id );

create policy "Users can update own profile"
  on public.users for update using ( auth.uid() = id );

create or replace function public.handle_new_user()
returns trigger as $$
begin
  insert into public.users (id, email, full_name, avatar_url)
  values (
    new.id,
    new.email,
    new.raw_user_meta_data->>'full_name',
    new.raw_user_meta_data->>'avatar_url'
  )
  on conflict (id) do nothing;
  return new;
end;
$$ language plpgsql security definer;

drop trigger if exists on_auth_user_created on auth.users;
create trigger on_auth_user_created
  after insert on auth.users
  for each row execute procedure public.handle_new_user();


-- 2. CHALLENGES TABLE
create table if not exists public.challenges (
  id uuid default gen_random_uuid() primary key,
  title text not null,
  description text,
  cover_image_url text,
  reward_gc int default 0,
  start_date timestamptz,
  end_date timestamptz,
  is_active boolean default true,
  created_at timestamptz default now(),
  updated_at timestamptz default now()
);

alter table public.challenges add column if not exists updated_at timestamptz default now();
alter table public.challenges enable row level security;

create policy "Public read access for challenges"
  on public.challenges for select using ( true );


-- 3. MISSIONS TABLE (with steps for briefing)
create table if not exists public.missions (
  id uuid default gen_random_uuid() primary key,
  title text not null,
  description text,
  icon_type text,
  image_url text,
  gc_reward int default 0,
  category text,
  is_active boolean default true,
  steps jsonb default '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]',
  challenge_id uuid references public.challenges(id),
  created_at timestamptz default now(),
  updated_at timestamptz default now()
);

alter table public.missions add column if not exists image_url text;
alter table public.missions add column if not exists category text;
alter table public.missions add column if not exists is_active boolean default true;
alter table public.missions add column if not exists steps jsonb default '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]';
alter table public.missions add column if not exists updated_at timestamptz default now();

alter table public.missions enable row level security;

create policy "Public read access for missions"
  on public.missions for select using ( true );


-- 4. USER_CHALLENGES TABLE (track joined challenges)
create table if not exists public.user_challenges (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) not null,
  challenge_id uuid references public.challenges(id) not null,
  challenge_score int default 0,
  joined_at timestamptz default now(),
  unique(user_id, challenge_id)
);

alter table public.user_challenges enable row level security;

create policy "Users can read own challenge joins"
  on public.user_challenges for select using ( auth.uid() = user_id );

create policy "Users can insert own challenge joins"
  on public.user_challenges for insert with check ( auth.uid() = user_id );


-- 5. SUBMISSIONS TABLE
create table if not exists public.submissions (
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
  status text default 'pending',
  rejected_reason text,
  created_at timestamptz default now()
);

alter table public.submissions enable row level security;

create policy "Users can view own submissions"
  on public.submissions for select using ( auth.uid() = user_id );

create policy "Users can create submissions"
  on public.submissions for insert with check ( auth.uid() = user_id );


-- 6. REWARDS TABLE (SHOP)
create table if not exists public.rewards (
  id uuid default gen_random_uuid() primary key,
  title text not null,
  description text,
  category text not null,
  gc_cost int default 0,
  image_url text,
  discount_label text,
  stock int default -1,
  is_active boolean default true,
  created_at timestamptz default now(),
  updated_at timestamptz default now()
);

alter table public.rewards add column if not exists stock int default -1;
alter table public.rewards add column if not exists is_active boolean default true;
alter table public.rewards add column if not exists updated_at timestamptz default now();

alter table public.rewards enable row level security;

create policy "Public read access for rewards"
  on public.rewards for select using ( true );


-- 7. TRANSACTIONS TABLE
create table if not exists public.transactions (
  id uuid default gen_random_uuid() primary key,
  user_id uuid references public.users(id) not null,
  amount int not null,
  description text,
  type text,
  related_submission_id uuid references public.submissions(id),
  related_reward_id uuid references public.rewards(id),
  created_at timestamptz default now()
);

alter table public.transactions enable row level security;

create policy "Users can read own transactions"
  on public.transactions for select using ( auth.uid() = user_id );

create policy "Users can insert own transactions"
  on public.transactions for insert with check ( auth.uid() = user_id );


-- 8. FAQ TABLE (for Help screen dynamic content)
create table if not exists public.faq (
  id uuid default gen_random_uuid() primary key,
  question text not null,
  answer text not null,
  sort_order int default 0,
  created_at timestamptz default now(),
  updated_at timestamptz default now()
);

alter table public.faq enable row level security;

create policy "Public read access for FAQ"
  on public.faq for select using ( true );


-- 9. REWARD_CATEGORIES TABLE (for Shop categories)
create table if not exists public.reward_categories (
  id uuid default gen_random_uuid() primary key,
  name text not null unique,
  sort_order int default 0,
  created_at timestamptz default now()
);

alter table public.reward_categories enable row level security;

create policy "Public read access for reward categories"
  on public.reward_categories for select using ( true );


-- SEED DATA

-- Reward Categories
insert into public.reward_categories (name, sort_order) values
('Travel', 1),
('Eco Store', 2),
('Lifestyle', 3),
('Direct Donate', 4),
('Food & Beverage', 5)
on conflict (name) do nothing;

-- Missions (run once; existing DB may already have these)
insert into public.missions (title, description, icon_type, gc_reward, steps)
select * from (values
  ('Green Canopy', 'Plant a native tree', 'TreePine', 250, '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]'::jsonb),
  ('Cycle Loop', 'Verify recycling batch', 'Recycle', 150, '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]'::jsonb),
  ('Plastic-Free', 'Cleanup plastic waste', 'Leaf', 100, '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]'::jsonb),
  ('Community Pulse', 'NGO volunteer work', 'Users', 300, '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]'::jsonb),
  ('Eco-Clearance', 'Garbage cleanup', 'Trash2', 200, '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]'::jsonb),
  ('Wildcard', 'Propose eco-action', 'Zap', 50, '["Prepare for the mission action", "Perform the eco-friendly task", "Take a photo as proof"]'::jsonb)
) v(title, description, icon_type, gc_reward, steps)
where not exists (select 1 from public.missions limit 1);

-- Challenges
insert into public.challenges (title, description, reward_gc, end_date, cover_image_url)
select * from (values
  ('City Cleanup Drive', 'Join the city-wide cleanup marathon.', 800, now() + interval '2 days', 'https://images.unsplash.com/photo-1757801720436-032c2e5b58c6?q=80&w=400'),
  ('Solar Transition', 'Switch to solar energy solutions.', 1500, now() + interval '12 days', 'https://images.unsplash.com/photo-1759266039803-1f81c04bd4c0?q=80&w=400'),
  ('Green Roof Initiative', 'Promoting green rooftops.', 5000, now() + interval '30 days', 'https://images.unsplash.com/photo-1607194402064-d0742de6d17b?q=80&w=600'),
  ('Coral Reef Revival', 'Protect our oceans.', 0, now() + interval '60 days', 'https://images.unsplash.com/photo-1741704445331-83ed820f0214?q=80&w=600')
) v(title, description, reward_gc, end_date, cover_image_url)
where not exists (select 1 from public.challenges limit 1);

-- Rewards
insert into public.rewards (title, category, gc_cost, image_url, discount_label)
select * from (values
  ('Metro Pass (1 Month)', 'Travel', 1200, 'https://images.unsplash.com/photo-1712591009476-5fe03c2ea938?q=80&w=400', '20% OFF'),
  ('Eco Bottle Pro', 'Eco Store', 800, 'https://images.unsplash.com/photo-1760863264228-fa0792a2d894?q=80&w=400', 'FREE'),
  ('Forest Donation', 'Direct Donate', 500, 'https://images.unsplash.com/photo-1647220576336-f2e94680f3b8?q=80&w=400', null),
  ('Zero Waste Kit', 'Lifestyle', 1500, 'https://images.unsplash.com/photo-1759868412016-8b7da190992a?q=80&w=400', '15% OFF'),
  ('₹50 Metro Recharge', 'Travel', 250, null, null),
  ('Bus Pass Discount', 'Travel', 400, null, null),
  ('Reusable Bottle', 'Eco Store', 300, null, null),
  ('Cloth Tote Bag', 'Eco Store', 200, null, null),
  ('Café Voucher', 'Lifestyle', 350, null, null)
) v(title, category, gc_cost, image_url, discount_label)
where not exists (select 1 from public.rewards limit 1);

-- FAQ (Help screen)
insert into public.faq (question, answer, sort_order)
select * from (values
  ('How are missions verified?', 'We use a combination of AI image recognition, metadata validation (GPS/Timestamp), and community peer-review to ensure every action is genuine.', 1),
  ('What can I buy with GreenCoins?', 'GreenCoins can be redeemed for sustainable products, public transport passes, or converted into direct donations for certified eco-projects.', 2),
  ('How do I level up?', 'Earn XP by completing missions and challenges. Higher levels unlock exclusive high-reward missions and limited edition rewards.', 3)
) v(question, answer, sort_order)
where not exists (select 1 from public.faq limit 1);

-- Storage: Create buckets 'mission-proofs' and 'avatars' in Supabase Dashboard
