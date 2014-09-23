% card io testing
%
% Milli [23th September 2014]

clear all;
close all;

% read image
I = imread('1-0.png');
Ig = rgb2gray(I);

% size for ref
s = size(Ig);
printf('dimensions of the image %d X %d\n', s(1), s(2));

% Construct inp pixel set
data = zeros(s(1)*s(2), 3);

rowno = 1;
for(i = 1:s(1))
	for(j = 1:s(2))
		pix = Ig(i, j);
				
		% set the point in data
		data(rowno, :) = [i, j, pix];
		rowno++;		
	endfor
endfor

% run db scan here
epsv = 2.5;
minp = 4;
printf('Running DBSCAN with eps : %d and minPoints : %d \n', epsv, minp);
[C, ptsC, centres] = dbscan(data', epsv, minp);

% process the clusters
cs = size(C);
noc = length(C);
printf('No of clusters : %d \n', noc);

clenghts = zeros(1, noc);
for(i = 1:noc)
	clengths(1, i) = length(C{i}); 
end

% check for the cluster with max size
[cSize, cNo] = max(clengths);
maxClus = C{cNo}; 

% redo the image
dim = zeros(s);
for(i = 1:cSize)
	pix = data(maxClus(i), :);
	dim(pix(1), pix(2)) = pix(3);
end

figure;
imshow(Ig);
figure;
imshow(dim);











